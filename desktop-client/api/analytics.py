import requests
from config import API_GATEWAY_URL
from api.auth import get_token

def get_headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }

def get_by_greenhouse(greenhouse_id, from_dt, to_dt):
    response = requests.get(
        f"{API_GATEWAY_URL}/api/analytics/greenhouse/{greenhouse_id}",
        params={"from": from_dt, "to": to_dt},
        headers=get_headers()
    )
    return response.json()

def get_average(greenhouse_id, from_dt, to_dt):
    response = requests.get(
        f"{API_GATEWAY_URL}/api/analytics/greenhouse/{greenhouse_id}/average",
        params={"from": from_dt, "to": to_dt},
        headers=get_headers()
    )
    return response.json()