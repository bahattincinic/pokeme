import hashlib
import binascii
import requests
import json

from requests import HTTPError


def hash_password(password: str, settings) -> str:
    dk = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode(),
        settings['SALT'].encode(), 100000
    )
    return binascii.hexlify(dk).decode("utf-8")


def send_push_notification(title: str, text: str, device_token: str,
                           credential: str) -> bool:
    url = "https://fcm.googleapis.com/fcm/send"
    body = {
        "to": device_token,
        "data": {
            "title": title,
            "message": text
        }
    }
    headers = {
        'Authorization': f'key={credential}',
        'Content-Type': 'application/json'
    }
    try:
        response = requests.post(url, data=json.dumps(body),
                                 headers=headers)
        response.raise_for_status()
    except HTTPError:
        return False
    return True
