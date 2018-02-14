from apistar import Include, Route
from apistar.handlers import docs_urls, static_urls
from apistar_token_auth.handlers import sqlalcamy_get_token

from pokeme.views import (
    user_profile, signup, create_note, update_note, delete_note,
    list_notes, create_todo, update_todo, delete_todo, list_todos,
    update_profile
)


routes = [
    Route('/me/', 'GET', user_profile, 'Show-User-Profile'),
    Route('/me/', 'PUT', user_profile, 'Update-Profile'),
    Route('/signup/', 'POST', signup, 'Signup'),
    Route('/token/', 'POST', sqlalcamy_get_token, 'Login'),
    Route('/notes/', 'GET', list_notes, 'List-Notes'),
    Route('/notes/', 'POST', create_note, 'Create-Note'),
    Route('/notes/{note}/', 'DELETE', delete_note, 'Delete-Note'),
    Route('/notes/{note}/', 'PUT', update_note, 'Update-Note'),
    Route('/todos/', 'GET', list_todos, 'List-Todos'),
    Route('/todos/', 'POST', create_todo, 'Create-Todo'),
    Route('/todos/{todo}/', 'DELETE', delete_todo, 'Delete-Todo'),
    Route('/todos/{todo}/', 'PUT', update_todo, 'Update-Todo'),
    Include('/docs', docs_urls),
    Include('/static', static_urls)
]
