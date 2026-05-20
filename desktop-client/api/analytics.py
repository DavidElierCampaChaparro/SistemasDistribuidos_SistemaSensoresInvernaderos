import requests
from config import API_GATEWAY_URL
from api.auth import get_token
 
 
def _headers():
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {get_token()}"
    }
 
 
def get_by_greenhouse(greenhouse_id, from_dt, to_dt):
    r = requests.get(
        f"{API_GATEWAY_URL}/api/analytics/greenhouse/{greenhouse_id}",
        params={"from": from_dt, "to": to_dt},
        headers=_headers(), timeout=15
    )
    r.raise_for_status()
    return r.json()
 
 
def get_average(greenhouse_id, from_dt, to_dt):
    r = requests.get(
        f"{API_GATEWAY_URL}/api/analytics/greenhouse/{greenhouse_id}/average",
        params={"from": from_dt, "to": to_dt},
        headers=_headers(), timeout=15
    )
    r.raise_for_status()
    return r.json()