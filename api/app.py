
from apistar.frameworks.asyncio import ASyncIOApp as App
from apistar.backends import sqlalchemy_backend

from pokeme.routes import routes
from pokeme.settings import settings
from pokeme.commands import commands


app = App(
    routes=routes,
    settings=settings,
    components=sqlalchemy_backend.components,
    commands=commands
)

if __name__ == '__main__':
    app.main()
