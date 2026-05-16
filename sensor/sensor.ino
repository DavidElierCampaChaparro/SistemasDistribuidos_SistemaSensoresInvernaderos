#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <ArduinoJson.h>

#define SERVICE_UUID        "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"

const char* formats[] = {
    "BRAND_B_V2_JSON",
    "BRAND_A_X100_JSON",
    "BRAND_C_PRO_XML"
};

const char* serials[] = {
    "SENSOR-001",
    "SENSOR-002",
    "SENSOR-003"
};

int formatIndex = 0;

BLECharacteristic* pCharacteristic;
bool deviceConnected = false;

class ServerCallbacks : public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
        deviceConnected = true;
        Serial.println("Client connected");
    }
    void onDisconnect(BLEServer* pServer) {
        deviceConnected = false;
        Serial.println("Client disconnected");
        BLEDevice::startAdvertising();
    }
};

void setup() {
    Serial.begin(115200);

    BLEDevice::init("IoT-Sensor-Gateway");
    BLEServer* pServer = BLEDevice::createServer();
    pServer->setCallbacks(new ServerCallbacks());

    BLEService* pService = pServer->createService(SERVICE_UUID);
    pCharacteristic = pService->createCharacteristic(
        CHARACTERISTIC_UUID,
        BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_NOTIFY
    );
    pCharacteristic->addDescriptor(new BLE2902());

    pService->start();
    BLEDevice::startAdvertising();
    Serial.println("ESP32 ready, waiting for BLE connection...");
}

void loop() {
    if (deviceConnected) {
        float temperature = random(15, 45) + random(0, 100) / 100.0;
        float humidity = random(30, 95) + random(0, 100) / 100.0;

        String format = formats[formatIndex % 3];
        String serial = serials[formatIndex % 3];
        formatIndex++;

        JsonDocument doc;
        doc["sensorSerialNumber"] = serial;
        doc["format"] = format;
        doc["temperature"] = temperature;
        doc["humidity"] = humidity;

        String jsonString;
        serializeJson(doc, jsonString);

        pCharacteristic->setValue(jsonString.c_str());
        pCharacteristic->notify();

        Serial.println("Sent: " + jsonString);
        delay(2000);
    }
}