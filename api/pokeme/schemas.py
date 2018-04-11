from apistar import typesystem


class Signup(typesystem.Object):
    properties = {
        'username': typesystem.String,
        'password': typesystem.String
    }
    required = ['username', 'password']


class ProfileUpdate(typesystem.Object):
    properties = {
        'username': typesystem.String,
        'password': typesystem.String
    }
    required = ['username']


class CategoryCreate(typesystem.Object):
    properties = {
        'name': typesystem.String,
    }
    required = ['name']


class CategoryList(typesystem.Object):
    properties = {
        'name': typesystem.String,
        'id': typesystem.Integer
    }


class NoteList(typesystem.Object):
    properties = {
        'title': typesystem.String,
        'text': typesystem.String,
        'id': typesystem.Integer,
        'category': CategoryList,
        'created_at': typesystem.String,
        'reminder_date': typesystem.String
    }


class NoteCreate(typesystem.Object):
    properties = {
        'title': typesystem.String,
        'text': typesystem.String,
        'category': typesystem.Integer,
        'reminder_date': typesystem.String,
        'device_token': typesystem.String
    }
    required = ['title', 'description', 'device_token']
