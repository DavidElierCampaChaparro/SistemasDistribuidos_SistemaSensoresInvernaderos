import requests
from config import API_GATEWAY_URL
from api.auth import get_token

def get_headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }

def get_all():
    response = requests.get(f"{API_GATEWAY_URL}/api/greenhouses", headers=get_headers())
    return response.json()

def get_by_id(id):
    response = requests.get(f"{API_GATEWAY_URL}/api/greenhouses/{id}", headers=get_headers())
    return response.json()

def create(name, location, owner_id, trigger_temp, trigger_humidity, notification_email):
    payload = {
        "name": name,
        "location": location,
        "ownerId": owner_id,
        "triggerTemperature": trigger_temp,
        "triggerHumidity": trigger_humidity,
        "notificationEmail": notification_email
    }
    response = requests.post(f"{API_GATEWAY_URL}/api/greenhouses", json=payload, headers=get_headers())
    return response.json()

def update(id, name, location, trigger_temp, trigger_humidity, notification_email):
    payload = {
        "name": name,
        "location": location,
        "triggerTemperature": trigger_temp,
        "triggerHumidity": trigger_humidity,
        "notificationEmail": notification_email
    }
    response = requests.put(f"{API_GATEWAY_URL}/api/greenhouses/{id}", json=payload, headers=get_headers())
    return response.json()

def delete(id):
    response = requests.delete(f"{API_GATEWAY_URL}/api/greenhouses/{id}", headers=get_headers())
    return response.status_code == 204