from apistar import typesystem


class NoteCreate(typesystem.Object):
    properties = {
        'title': typesystem.String,
        'text': typesystem.String
    }
    required = ['title', 'description']


class Signup(typesystem.Object):
    properties = {
        'username': typesystem.String(),
        'password': typesystem.String()
    }
    required = ['username', 'password']


class ProfileUpdate(typesystem.Object):
    properties = {
        'username': typesystem.String(),
        'password': typesystem.String()
    }
    required = ['username']


class NoteList(typesystem.Object):
    properties = {
        'title': typesystem.String(),
        'text': typesystem.String(),
        'id': typesystem.Integer(),
        'created_at': typesystem.String()
    }


class CategoryCreate(typesystem.Object):
    properties = {
        'name': typesystem.String(),
    }
    required = ['name']


class CategoryList(typesystem.Object):
    properties = {
        'name': typesystem.String(),
        'id': typesystem.Integer()
    }
