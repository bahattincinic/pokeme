import pytest

from apistar import TestClient, reverse_url

from app import app


user = {
    'username': 'test',
    'password': 123456
}


@pytest.fixture
def setup_tables(scope="function"):
    app.main(['create_tables'])
    yield
    app.main(['drop_tables'])


def test_signup(setup_tables):
    client = TestClient(app)
    response = client.post(reverse_url('Signup'), json=user)
    assert response.status_code == 200
    assert response.json()['username'] == user['username']


def test_login(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    response = client.post(reverse_url('Login'), json=user)
    assert response.status_code == 200


def test_user_profile(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }
    response = client.get(reverse_url('Show-User-Profile'),
                          headers=headers)
    assert response.status_code == 200
    assert response.json()['username'] == user['username']
