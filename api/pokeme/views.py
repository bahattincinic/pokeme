from apistar import Response, annotate, Settings
from apistar.backends.sqlalchemy_backend import Session
from apistar.interfaces import Auth
from apistar.permissions import IsAuthenticated

from apistar_token_auth.authentication import SQLAlchemyTokenAuthentication
from .schemas import Signup
from .models import User
from .utils import hash_password


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def user_profile(session: Session, auth: Auth):
    """
    This endpoint returns current user profile
    """
    return {
        'username': auth.user.username,
        'id': auth.user.id
    }


def signup(session: Session, data: Signup, settings: Settings):
    user = User(
        username=data['username'],
        password=hash_password(data['password'], settings)
    )

    session.add(user)
    session.flush()

    return {
        'id': user.id,
        'username': user.username
    }
