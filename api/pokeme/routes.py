from apistar import Include, Route
from apistar.handlers import docs_urls, static_urls
from apistar_token_auth.handlers import sqlalcamy_get_token

from pokeme.views import (
    user_profile, signup, create_note, update_note, delete_note,
    list_notes, create_category, update_category, delete_category, list_categories,
    update_profile, list_category_notes
)


routes = [
    Route('/me/', 'GET', user_profile, 'Show-User-Profile'),
    Route('/me/', 'PUT', update_profile, 'Update-Profile'),
    Route('/signup/', 'POST', signup, 'Signup'),
    Route('/token/', 'POST', sqlalcamy_get_token, 'Login'),
    Route('/notes/', 'GET', list_notes, 'List-Notes'),
    Route('/notes/', 'POST', create_note, 'Create-Note'),
    Route('/notes/{note}/', 'DELETE', delete_note, 'Delete-Note'),
    Route('/notes/{note}/', 'PUT', update_note, 'Update-Note'),
    Route('/categories/', 'GET', list_categories, 'List-Categories'),
    Route('/categories/', 'POST', create_category, 'Create-Category'),
    Route('/categories/{category}/', 'DELETE', delete_category, 'Delete-Category'),
    Route('/categories/{category}/', 'PUT', update_category, 'Update-Category'),
    Route('/categories/{category}/notes/', 'GET', list_category_notes, 'Category-Notes'),
    Include('/docs', docs_urls),
    Include('/static', static_urls)
]
