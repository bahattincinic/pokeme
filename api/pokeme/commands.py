import datetime
import schedule
import time
import pytz

from functools import partial

from apistar import Command
from apistar.backends.sqlalchemy_backend import (
    create_tables, drop_tables, SQLAlchemyBackend
)

from .models import Note, User
from .utils import send_push_notification
from .settings import settings


def check_notification(session):
    print("Check Notification Schedule...")

    now = datetime.datetime.now(
        pytz.timezone(settings['TIMEZONE'])
    ).strftime('%Y-%m-%d %H:%M:%S')

    notes = session.query(Note).join(User, User.id == Note.user_id).filter(
        Note.is_notification_send is False,
        Note.reminder_date.isnot(None),
        Note.reminder_date < now
    )

    for note in notes.all():
        send_push_notification(
            title=note.title,
            text=f"Pokeme {note.title} notification",
            device_token=note.device_token,
            credential=settings['FIREBASE_TOKEN']
        )
        note.is_notification_send = True
        session.commit()
        print("%s note notification was sent" % note.id)


def check_schedule(backend: SQLAlchemyBackend):
    session = backend.Session()

    try:
        schedule.every(1).minutes.do(partial(
            check_notification, session=session
        ))

        while True:
            schedule.run_pending()
            time.sleep(1)
    finally:
        session.close()


commands = [
    Command('schedule', check_schedule),
    Command('create_tables', create_tables),
    Command('drop_tables', drop_tables)
]
