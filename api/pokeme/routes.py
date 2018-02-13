from apistar import Include, Route
from apistar.handlers import docs_urls, static_urls
from apistar_token_auth.handlers import sqlalcamy_get_token

from pokeme.views import user_profile, signup


routes = [
    Route('/me', 'GET', user_profile, 'User-Profile'),
    Route('/signup', 'POST', signup, 'Signup'),
    Route('/token', 'POST', sqlalcamy_get_token, 'Login'),
    Include('/docs', docs_urls),
    Include('/static', static_urls)
]
