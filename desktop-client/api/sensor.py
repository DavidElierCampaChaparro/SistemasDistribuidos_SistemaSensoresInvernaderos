import requests
from config import API_GATEWAY_URL
from api.auth import get_token
 
 
def _headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }
 
 
def get_by_greenhouse(greenhouse_id):
    r = requests.get(f"{API_GATEWAY_URL}/api/sensors/greenhouse/{greenhouse_id}",
                     headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def create(serial_number, greenhouse_id, fmt):
    payload = {"serialNumber": serial_number, "greenhouseId": greenhouse_id, "format": fmt}
    r = requests.post(f"{API_GATEWAY_URL}/api/sensors", json=payload, headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def update(id, serial_number, greenhouse_id, fmt):
    payload = {"serialNumber": serial_number, "greenhouseId": greenhouse_id, "format": fmt}
    r = requests.put(f"{API_GATEWAY_URL}/api/sensors/{id}", json=payload, headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def delete(id):
    r = requests.delete(f"{API_GATEWAY_URL}/api/sensors/{id}", headers=_headers(), timeout=10)
    return r.status_code == 204