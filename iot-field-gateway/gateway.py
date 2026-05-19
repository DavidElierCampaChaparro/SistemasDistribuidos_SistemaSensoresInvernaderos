import asyncio
import os
import requests
import xml.etree.ElementTree as ET
from bleak import BleakScanner, BleakClient
import json

# Configuration
SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
CHARACTERISTIC_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"
AUTH_SERVICE_URL = os.getenv("AUTH_SERVICE_URL", "http://host.docker.internal:8090/ws")
API_GATEWAY_URL = os.getenv("API_GATEWAY_URL", "http://host.docker.internal:8080/api/ingest")

# System credentials
SYSTEM_EMAIL = "romomanulo@gmail.com"
SYSTEM_PASSWORD = "1234"

token = None

def login():
    global token
    soap_body = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:LoginRequest>
                <auth:email>{SYSTEM_EMAIL}</auth:email>
                <auth:password>{SYSTEM_PASSWORD}</auth:password>
            </auth:LoginRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    """
    headers = {"Content-Type": "text/xml;charset=UTF-8", "SOAPAction": ""}
    response = requests.post(AUTH_SERVICE_URL, data=soap_body, headers=headers, timeout=15)

    if response.status_code != 200:
        raise Exception(f"Login fallido: HTTP {response.status_code} - {response.text}")
    
    root = ET.fromstring(response.text)
    ns = {"soap": "http://schemas.xmlsoap.org/soap/envelope/",
          "auth": "http://auth-service.dev/soap/auth"}
    token_element = root.find(".//auth:token", ns)
    
    if token_element is not None and token_element.text:
        token = token_element.text
        print(f"Login exitoso, token obtenido")
    else:
        raise Exception("Login fallido")

def forward_to_gateway(sensor_data: dict):
    global token
    
    # Creates raw data depending on format
    format_type = sensor_data.get("format")
    temperature = sensor_data.get("temperature")
    humidity = sensor_data.get("humidity")
    
    if format_type == "BRAND_B_V2_JSON":
        raw_data = json.dumps({
            "temperature": temperature,
            "relative_humidity": humidity
        })
    elif format_type == "BRAND_A_X100_JSON":
        raw_data = json.dumps({
            "temp_f": round(temperature * 9/5 + 32, 2),
            "hum": round(humidity / 100, 2)
        })
    elif format_type == "BRAND_C_PRO_XML":
        raw_data = f"<reading><celsius>{temperature}</celsius><humidity>{humidity}</humidity></reading>"
    else:
        raw_data = json.dumps({"temperature": temperature, "relative_humidity": humidity})

    payload = {
        "sensorSerialNumber": sensor_data.get("sensorSerialNumber"),
        "rawData": raw_data
    }

    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {token}"
    }

    response = requests.post(API_GATEWAY_URL, json=payload, headers=headers, timeout=15)
    
    if response.status_code == 401:
        print("Token expired, renewing...")
        login()
        headers["Authorization"] = f"Bearer {token}"
        response = requests.post(API_GATEWAY_URL, json=payload, headers=headers, timeout=15)
    
    if response.status_code == 200:
        print(f"Data sent successfully: {payload}")
    else:
        print(f"Error sending data: {response.status_code} - {response.text}")

def notification_handler(sender, data):
    try:
        json_str = data.decode("utf-8")
        sensor_data = json.loads(json_str)
        print(f"Received from ESP32: {sensor_data}")
        forward_to_gateway(sensor_data)
    except Exception as e:
        print(f"Error proccessing data: {e}")

async def main():
    print("Initiating IoT Field Gateway...")
    login()
    
    print("Scanning BLE devices...")
    devices = await BleakScanner.discover(timeout=10)
    
    target = None
    for device in devices:
        if device.name == "IoT-Sensor-Gateway":
            target = device
            break
    
    if not target:
        print("ESP32 not found. Make sure it's turned on and nearby.")
        return
    
    print(f"ESP32 found: {target.address}")
    
    async with BleakClient(target.address) as client:
        print("Connected to ESP32")
        await client.start_notify(CHARACTERISTIC_UUID, notification_handler)
        print("Listening sensor data... (Ctrl+C to stop)")
        
        while True:
            await asyncio.sleep(1)

if __name__ == "__main__":
    asyncio.run(main())