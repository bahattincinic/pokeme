from apistar import Response, annotate, Settings
from apistar.backends.sqlalchemy_backend import Session
from apistar.interfaces import Auth
from apistar.permissions import IsAuthenticated
from apistar.exceptions import NotFound, ValidationError

from apistar_token_auth.authentication import SQLAlchemyTokenAuthentication
from apistar_token_auth.utils import generate_key
from .schemas import Signup
from .models import User, Note, Category, AccessToken
from .utils import hash_password
from .schemas import (
    NoteCreate, NoteList, CategoryCreate, CategoryList,
    ProfileUpdate
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
def update_profile(session: Session, auth: Auth, data: ProfileUpdate,
                   settings: Settings):
    """
    This endpoint updates current user profile
    """
    user = session.query(User).filter(User.id == auth.user.id).first()

    if data['username'] != user.username and session.query(User).filter(
            User.username == data['username']).count() != 0:
        raise ValidationError({'message': 'Username already in use'})

    user.username = data['username']

    if data.get('password'):
        user.password = hash_password(data['password'], settings)

    session.commit()
    return {
        'username': user.username,
        'id': user.id
    }


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

    if data.get('reminder_date'):
        instance.reminder_date = data['reminder_date']
    
    if data.get('category'):
        category = session.query(Category).filter(
            Category.id == data['category'],
            Category.user_id == auth.user.id
        ).first()
        
        if not category:
            raise ValidationError({'message': 'Invalid category'})

        instance.category_id = category.id

    if data.get('is_archived'):
        instance.is_archived = data['is_archived']

    session.add(instance)
    session.flush()
    return NoteList(instance)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def update_note(session: Session, note: int, auth: Auth, data: NoteCreate):
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

    if data.get('reminder_date'):
        instance.reminder_date = data['reminder_date']
    
    if data.get('category'):
        category = session.query(Category).filter(
            Category.id == data['category'],
            Note.user_id == auth.user.id
        ).first()
        
        if not category:
            raise ValidationError({'message': 'Invalid category'})

        instance.category_id = category.id
    
    if data.get('is_archived'):
        instance.is_archived = data['is_archived']

    session.commit()
    return NoteList(session.query(Note).filter(Note.id == note).first())


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def delete_note(session: Session, note: int, auth: Auth):
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
    return Response(
        status=200,
        content={'message': 'Note Deleted'}
    )


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def list_notes(session: Session, auth: Auth):
    """
    This endpoint shows notes
    """
    notes = session.query(Note).filter(
        Note.user_id == auth.user.id
    ).outerjoin(Category, Category.id == Note.category_id).all()

    response = []
    for note in notes:
        props = {
            'title': note.title,
            'text': note.text,
            'id': note.id,
            'is_archived': note.is_archived,
            'reminder_date': note.reminder_date,
            'created_at': note.created_at
        }
        if note.category:
            props['category'] = CategoryList(note.category)

        response.append(NoteList(**props))
    return {
        'count': len(response),
        'results': response
    }


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def create_category(session: Session, data: CategoryCreate, auth: Auth):
    """
    This endpoint creates category
    """
    instance = Category(
        user_id=auth.user.id,
        name=data['name']
    )
    session.add(instance)
    session.flush()
    return CategoryList(instance)


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def update_category(session: Session, category: int, auth: Auth, data: CategoryCreate):
    """
    This endpoint updated category
    """
    instance = session.query(Category).filter(
        Category.id == category,
        Category.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Category not found'})

    instance.name = data['name']
    session.commit()
    return CategoryList(
        session.query(Category).filter(
            Category.id == instance.id
        ).first()
    )


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def delete_category(session: Session, category: int, auth: Auth):
    """
    This endpoint deletes category
    """
    instance = session.query(Category).filter(
        Category.id == category,
        Category.user_id == auth.user.id
    ).first()

    if not instance:
        raise NotFound({'message': 'Category not found'})

    session.delete(instance)
    session.commit()
    return Response(
        status=200,
        content={'message': 'Category Deleted'}
    )


@annotate(authentication=[SQLAlchemyTokenAuthentication()],
          permissions=[IsAuthenticated()])
def list_categories(session: Session, auth: Auth):
    """
    This endpoint shows categories
    """
    categories = session.query(Category).filter(
        Category.user_id == auth.user.id).all()

    response = [
        CategoryList(category)
        for category in categories
    ]

    return {
        'count': len(response),
        'results': response
    }
