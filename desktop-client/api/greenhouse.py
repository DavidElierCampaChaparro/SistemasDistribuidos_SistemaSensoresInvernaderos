import requests
from config import API_GATEWAY_URL
from api.auth import get_token
 
 
def _headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }
 
 
def get_all():
    r = requests.get(f"{API_GATEWAY_URL}/api/greenhouses", headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def get_by_id(id):
    r = requests.get(f"{API_GATEWAY_URL}/api/greenhouses/{id}", headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def create(name, location, owner_id, trigger_temp, trigger_humidity):
    payload = {
        "name": name,
        "location": location,
        "ownerId": owner_id,
        "triggerTemperature": trigger_temp,
        "triggerHumidity": trigger_humidity,
    }
    r = requests.post(f"{API_GATEWAY_URL}/api/greenhouses", json=payload, headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def update(id, name, location, trigger_temp, trigger_humidity):
    payload = {
        "name": name,
        "location": location,
        "triggerTemperature": trigger_temp,
        "triggerHumidity": trigger_humidity,
    }
    r = requests.put(f"{API_GATEWAY_URL}/api/greenhouses/{id}", json=payload, headers=_headers(), timeout=10)
    r.raise_for_status()
    return r.json()
 
 
def delete(id):
    r = requests.delete(f"{API_GATEWAY_URL}/api/greenhouses/{id}", headers=_headers(), timeout=10)
    return r.status_code == 204