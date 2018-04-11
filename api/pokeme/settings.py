from apistar import environment, typesystem
from apistar.parsers import JSONParser

from .models import Base


class Env(environment.Environment):
    properties = {
        'POKEME_DEBUG': typesystem.boolean(default=False),
        'POKEME_DATABASE_URL': typesystem.string(),
    }
    required = ['POKEME_DEBUG', 'POKEME_DATABASE_URL']


env = Env()
settings = {
    'DEBUG': env['POKEME_DEBUG'],
    'DATABASE': {
        'URL': env['POKEME_DATABASE_URL'],
        'METADATA': Base.metadata
    },
    'PARSERS': [JSONParser()],
    'SCHEMA': {
        'TITLE': 'pokeme',
        'DESCRIPTION': 'To-do & Note Android App'
    },
    'SALT': '0394a2ede332c9a13eb82e9b24631604c31df978b4e2f0fbd2c549944f9d79a5',
    'TOKEN_AUTHENTICATION': {
        'IS_EXPIRY_TOKEN': True,
        'EXPIRY_TIME': 30,
        'USERNAME_FIELD': 'username',
        'PASSWORD_FIELD': 'password',
        'ORM': 'sqlalcamy',
        'USER_MODEL': 'pokeme.models.User',
        'TOKEN_MODEL': 'pokeme.models.AccessToken',
        'ENCRYPTION_FUNCTION': 'pokeme.utils.hash_password'
    },
    'FIREBASE_TOKEN': 'AAAAcD4r7wM:APA91bGxPuKUQUk9-pAu24AxNDy8_j3tjkJ-eOVyM4wDrhc6MrdnV6hhNEgJPvEhAIMdNeJoZt8vITwjYxIiAkK2x1XCIi9d_3RbLsn-pLmKdZtNfk1CmwW0S1F8umP46VQv_8ks9FPh'  # noqa
}
