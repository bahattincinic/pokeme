import hashlib
import binascii


def hash_password(password, settings):
    dk = hashlib.pbkdf2_hmac(
        'sha256',
        password.encode(),
        settings['SALT'].encode(), 100000
    )
    return binascii.hexlify(dk).decode("utf-8") 
