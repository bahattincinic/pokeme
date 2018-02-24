from typing import Any

from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import (
    Column, Integer, String, DateTime, func, Text, Boolean,
    ForeignKey
)

Base: Any = declarative_base()


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    username = Column(String)
    password = Column(String)


class AccessToken(Base):
    __tablename__ = 'access_tokens'

    id = Column(Integer, primary_key=True)
    token = Column(String)
    created_at = Column(DateTime, default=func.now())
    user_id = Column(Integer)


class Category(Base):
    __tablename__ = 'categories'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    user = relationship("User")

    name = Column(String)
    created_at = Column(DateTime, default=func.now())


class Note(Base):
    __tablename__ = 'notes'

    id = Column(Integer, primary_key=True)

    user_id = Column(Integer, ForeignKey("users.id"))
    user = relationship("User")
    category_id = Column(Integer, ForeignKey("categories.id"), nullable=True)
    category = relationship("Category")

    title = Column(String)
    text = Column(Text)
    created_at = Column(DateTime, default=func.now())

    is_archived = Column(Boolean, default=False)
    reminder_date = Column(DateTime, nullable=True)
