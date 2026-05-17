import requests
import xml.etree.ElementTree as ET
import base64
import json
from config import AUTH_SERVICE_URL

token = None

def login(email, password):
    global token
    soap_body = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:LoginRequest>
                <auth:email>{email}</auth:email>
                <auth:password>{password}</auth:password>
            </auth:LoginRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    """
    headers = {"Content-Type": "text/xml;charset=UTF-8", "SOAPAction": ""}
    response = requests.post(AUTH_SERVICE_URL, data=soap_body, headers=headers)
    root = ET.fromstring(response.text)
    ns = {
        "soap": "http://schemas.xmlsoap.org/soap/envelope/",
        "auth": "http://auth-service.dev/soap/auth"
    }
    success = root.find(".//auth:success", ns).text == "true"
    token_el = root.find(".//auth:token", ns)
    if success and token_el is not None:
        token = token_el.text
        return True, token
    message = root.find(".//auth:message", ns).text
    return False, message

def register(name, lastname, email, password):
    soap_body = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:RegisterRequest>
                <auth:name>{name}</auth:name>
                <auth:lastname>{lastname}</auth:lastname>
                <auth:email>{email}</auth:email>
                <auth:password>{password}</auth:password>
            </auth:RegisterRequest>
        </soapenv:Body>
    </soapenv:Envelope>
    """
    headers = {"Content-Type": "text/xml;charset=UTF-8", "SOAPAction": ""}
    response = requests.post(AUTH_SERVICE_URL, data=soap_body, headers=headers)
    root = ET.fromstring(response.text)
    ns = {"auth": "http://auth-service.dev/soap/auth"}
    success = root.find(".//auth:success", ns).text == "true"
    message = root.find(".//auth:message", ns).text
    return success, message

def get_token():
    return token

def get_user_id():
    if token is None:
        return None
    payload = token.split(".")[1]
    payload += "=" * (4 - len(payload) % 4)
    decoded = json.loads(base64.b64decode(payload))
    return decoded["userId"]