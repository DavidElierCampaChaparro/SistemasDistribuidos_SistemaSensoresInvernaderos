import requests
from config import API_GATEWAY_URL
from api.auth import get_token

def get_headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }

def get_by_greenhouse(greenhouse_id):
    response = requests.get(
        f"{API_GATEWAY_URL}/api/sensors/greenhouse/{greenhouse_id}",
        headers=get_headers()
    )
    return response.json()

def create(serial_number, greenhouse_id, format):
    payload = {
        "serialNumber": serial_number,
        "greenhouseId": greenhouse_id,
        "format": format
    }
    response = requests.post(f"{API_GATEWAY_URL}/api/sensors", json=payload, headers=get_headers())
    return response.json()

def delete(id):
    response = requests.delete(f"{API_GATEWAY_URL}/api/sensors/{id}", headers=get_headers())
    return response.status_code == 204