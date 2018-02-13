from apistar import typesystem


class Password(typesystem.String):
    description = 'Password'


class Username(typesystem.String):
    description = 'Username'


class NoteTitle(typesystem.String):
    description = 'Title'


class NoteDescription(typesystem.String):
    description = 'description'


class NoteId(typesystem.Integer):
    description = 'Note ID'


class NoteCreationDate(typesystem.String):
    description = 'Creation Date'


class NoteCreate(typesystem.Object):
    properties = {
        'title': NoteTitle,
        'text': NoteDescription
    }
    required = ['title', 'description']


class TodoTitle(typesystem.String):
    description = 'Title'


class TodoDescription(typesystem.String):
    description = 'description'


class TodoDueDate(typesystem.String):
    description = 'Due Date'


class TodoCreationDate(typesystem.String):
    description = 'Creation Date'


class Signup(typesystem.Object):
    properties = {
        'username': Username,
        'password': Password
    }
    required = ['username', 'password']


class NoteList(typesystem.Object):
    properties = {
        'title': NoteTitle,
        'text': NoteDescription,
        'id': NoteId,
        'created_at': NoteCreationDate
    }


class TodoCreate(typesystem.Object):
    properties = {
        'title': TodoTitle,
        'text': TodoDescription,
        'due_date': TodoDueDate,
        'is_completed': typesystem.Boolean
    }
    required = ['title', 'text']


class TodoList(typesystem.Object):
    properties = {
        'title': TodoTitle,
        'text': TodoDescription,
        'due_date': TodoDueDate,
        'is_completed': typesystem.Boolean,
        'created_at': TodoCreationDate
    }
