from apistar import Response, annotate, Settings
from apistar.backends.sqlalchemy_backend import Session
from apistar.interfaces import Auth
from apistar.permissions import IsAuthenticated

from apistar_token_auth.authentication import SQLAlchemyTokenAuthentication
from .schemas import Signup
from .models import User
from .utils import hash_password
from .schemas import (
    TodoCreate, TodoList, NoteCreate, NoteList, TodoId, NoteId
)


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


def create_note(session: Session, data: NoteCreate):
    pass


def update_note(session: Session, note: NoteId):
    pass


def delete_note(session: Session, note: NoteId):
    pass


def list_notes(session: Session):
    pass


def create_todo(session: Session, data: TodoCreate):
    pass


def update_todo(session: Session, todo: TodoId):
    pass


def delete_todo(session: Session, todo: TodoId):
    pass


def list_todos(session: Session):
    pass
