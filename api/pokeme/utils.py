import hashlib
import binascii
import requests
import json


def hash_password(password, settings):
    dk = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode(),
        settings['SALT'].encode(), 100000
    )
    return binascii.hexlify(dk).decode("utf-8") 


def send_push_notification(title, text, device_token, credential):
    url = "https://fcm.googleapis.com/fcm/send"
    body = {
        "to" : device_token,
        "data" : {
            "title": title,
            "message" : text
        }
    }
    headers = {
        'Authorization': f'key={credential}',
        'Content-Type': 'application/json'
    }
    requests.post(url, data=json.dumps(body), headers=headers)
