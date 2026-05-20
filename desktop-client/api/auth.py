import requests
import xml.etree.ElementTree as ET
import base64
import json
from config import AUTH_SERVICE_URL
 
_token = None
_user_id = None
_user_name = None
_user_lastname = None
_user_email = None
 
 
def _soap_post(body: str):
    headers = {"Content-Type": "text/xml;charset=UTF-8", "SOAPAction": ""}
    return requests.post(AUTH_SERVICE_URL, data=body, headers=headers, timeout=10)
 
 
def login(email, password):
    global _token, _user_id, _user_email
    soap = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:LoginRequest>
                <auth:email>{email}</auth:email>
                <auth:password>{password}</auth:password>
            </auth:LoginRequest>
        </soapenv:Body>
    </soapenv:Envelope>"""
    response = _soap_post(soap)
    root = ET.fromstring(response.text)
    ns = {"soap": "http://schemas.xmlsoap.org/soap/envelope/",
          "auth": "http://auth-service.dev/soap/auth"}
    success = root.find(".//auth:success", ns).text == "true"
    token_el = root.find(".//auth:token", ns)
    if success and token_el is not None:
        _token = token_el.text
        decoded = _decode_token(_token)
        _user_id = decoded.get("userId")
        _user_email = email
        _user_name = decoded.get("name")
        _user_lastname = decoded.get("lastname")
        return True, _token
 
 
def register(name, lastname, email, password):
    soap = f"""
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
    </soapenv:Envelope>"""
    response = _soap_post(soap)
    root = ET.fromstring(response.text)
    ns = {"auth": "http://auth-service.dev/soap/auth"}
    success = root.find(".//auth:success", ns).text == "true"
    message = root.find(".//auth:message", ns).text
    return success, message
 
 
def update_owner(user_id, name, lastname, email):
    soap = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:UpdateOwnerRequest>
                <auth:id>{user_id}</auth:id>
                <auth:name>{name}</auth:name>
                <auth:lastname>{lastname}</auth:lastname>
                <auth:email>{email}</auth:email>
            </auth:UpdateOwnerRequest>
        </soapenv:Body>
    </soapenv:Envelope>"""
    response = _soap_post(soap)
    root = ET.fromstring(response.text)
    ns = {"auth": "http://auth-service.dev/soap/auth"}
    success = root.find(".//auth:success", ns).text == "true"
    message = root.find(".//auth:message", ns).text
    if success:
        global _user_name, _user_lastname, _user_email
        _user_name = name
        _user_lastname = lastname
        _user_email = email
    return success, message
 
 
def delete_owner(user_id):
    soap = f"""
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:auth="http://auth-service.dev/soap/auth">
        <soapenv:Header/>
        <soapenv:Body>
            <auth:DeleteOwnerRequest>
                <auth:id>{user_id}</auth:id>
            </auth:DeleteOwnerRequest>
        </soapenv:Body>
    </soapenv:Envelope>"""
    response = _soap_post(soap)
    root = ET.fromstring(response.text)
    ns = {"auth": "http://auth-service.dev/soap/auth"}
    success = root.find(".//auth:success", ns).text == "true"
    message = root.find(".//auth:message", ns).text
    return success, message
 
 
def _decode_token(token):
    payload = token.split(".")[1]
    payload += "=" * (4 - len(payload) % 4)
    return json.loads(base64.b64decode(payload))
 
 
def get_token():
    return _token
 
 
def get_user_id():
    return _user_id
 
 
def get_user_email():
    return _user_email

def get_user_name():
    return _user_name

def get_user_lastname():
    return _user_lastname
 
 
def logout():
    global _token, _user_id, _user_name, _user_lastname, _user_email
    _token = _user_id = _user_name = _user_lastname = _user_email = None