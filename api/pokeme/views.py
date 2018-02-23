from apistar import Response, annotate, Settings
from apistar.backends.sqlalchemy_backend import Session
from apistar.interfaces import Auth
from apistar.permissions import IsAuthenticated
from apistar.exceptions import NotFound

from apistar_token_auth.authentication import SQLAlchemyTokenAuthentication
from apistar_token_auth.utils import generate_key
from .schemas import Signup
from .models import User, Note, Todo, AccessToken
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


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def update_profile(session: Session, auth: Auth, data: Signup,
                   settings: Settings):
    """
    This endpoint updates current user profile
    """
    user = session.query(User).filter(User.id == auth.user.id).first()
    user.username = data['username']
    user.password = hash_password(data['password'], settings)

    session.commit()
    return Response(status=200)


def signup(session: Session, data: Signup, settings: Settings):
    """
    This endpoint creates user
    """
    user = User(
        username=data['username'],
        password=hash_password(data['password'], settings)
    )

    session.add(user)
    session.flush()

    token = AccessToken(token=generate_key(), user_id=user.id)
    session.add(token)

    return {
        'id': user.id,
        'username': user.username,
        'token': token.token
    }


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def create_note(session: Session, data: NoteCreate, auth: Auth):
    """
    This endpoint created note
    """
    instance = Note(
        user_id=auth.user.id,
        title=data['title'],
        text=data['text']
    )
    session.add(instance)
    session.flush()
    return NoteList(instance)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def update_note(session: Session, note: NoteId, auth: Auth, data: NoteCreate):
    """
    This endpoint updates note
    """
    instance = session.query(Note).filter(
        Note.id == note,
        Note.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Note not found'})

    instance.title = data['title']
    instance.text = data['text']
    session.commit()
    return NoteList(session.query(Note).filter(Note.id == note).first())


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def delete_note(session: Session, note: NoteId, auth: Auth):
    """
    This endpoint deletes note
    """
    instance = session.query(Note).filter(
        Note.id == note,
        Note.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Note not found'})

    session.delete(instance)
    session.commit()
    return Response(status=204)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def list_notes(session: Session, auth: Auth):
    """
    This endpoint shows notes
    """
    notes = session.query(Note).filter(
        Note.user_id == auth.user.id).all()
    return [
        NoteList(note)
        for note in notes
    ]


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def create_todo(session: Session, data: TodoCreate, auth: Auth):
    """
    This endpoint creates todo
    """
    instance = Todo(
        user_id=auth.user.id,
        title=data['title'],
        text=data['text'],
        due_date=data['due_date'],
        is_completed=data['is_completed']
    )
    session.add(instance)
    session.flush()
    return TodoList(instance)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def update_todo(session: Session, todo: TodoId, auth: Auth, data: TodoCreate):
    """
    This endpoint updated todo
    """
    instance = session.query(Todo).filter(
        Todo.id == todo,
        Todo.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Todo not found'})

    instance.title = data['title']
    instance.text = data['text']
    instance.due_date = data['due_date']
    instance.is_completed = data['is_completed']
    session.commit()
    return TodoList(session.query(Todo).filter(Todo.id == todo).first())


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def delete_todo(session: Session, todo: TodoId, auth: Auth):
    """
    This endpoint deleted todo
    """
    instance = session.query(Todo).filter(
        Todo.id == todo,
        Todo.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Todo not found'})

    session.delete(instance)
    session.commit()
    return Response(status=204)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def list_todos(session: Session, auth: Auth):
    """
    This endpoint shows todos
    """
    todos = session.query(Todo).filter(
        Todo.user_id == auth.user.id).all()
    return [
        TodoList(todo)
        for todo in todos
    ]
