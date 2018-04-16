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


def test_update_profile(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }
    data = {
        'username': 'test2',
        'password': '1234567'
    }

    response = client.put(reverse_url('Update-Profile'),
                          headers=headers, json=data)
    assert response.status_code == 200
    assert response.json()['username'] == data['username']

    response2 = client.post(reverse_url('Login'), json=data)
    assert response2.status_code == 200


def test_create_category(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'name': 'Test Category'
    }

    response = client.post(reverse_url('Create-Category'),
                           json=data, headers=headers)
    assert response.status_code == 200


def test_list_categories(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'name': 'Test Category'
    }

    client.post(reverse_url('Create-Category'),
                json=data, headers=headers)

    response = client.get(reverse_url('List-Categories'),
                          headers=headers)
    content = response.json()
    assert response.status_code == 200
    assert content['count'] == 1
    assert len(content['results']) == 1
    assert content['results'][0]['name'] == data['name']


def test_delete_category(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'name': 'Test Category'
    }

    category = client.post(reverse_url('Create-Category'),
                           json=data, headers=headers).json()

    response = client.delete(
        reverse_url('Delete-Category',
                    category=category['id']),
        headers=headers
    )
    response1 = client.get(reverse_url('List-Categories'),
                           headers=headers)
    content = response1.json()

    assert response1.status_code == 200
    assert content['count'] == 0
    assert len(content['results']) == 0
    assert response.status_code == 200


def test_create_note(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'title': 'Test Note Title',
        'text': 'Test Note Test',
        'device_token': '123456'
    }

    response = client.post(reverse_url('Create-Note'),
                           json=data, headers=headers)
    content = response.json()

    assert content['title'] == data['title']
    assert content['text'] == data['text']
    assert response.status_code == 200


def test_delete_note(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'title': 'Test Note Title',
        'text': 'Test Note Test',
        'device_token': '123456'
    }

    note = client.post(reverse_url('Create-Note'),
                       json=data, headers=headers).json()

    response = client.delete(
        reverse_url('Delete-Note',
                    note=note['id']),
        headers=headers
    )
    response1 = client.get(reverse_url('List-Notes'),
                           headers=headers)
    content = response1.json()

    assert response1.status_code == 200
    assert content['count'] == 0
    assert len(content['results']) == 0
    assert response.status_code == 200


def test_list_notes(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    data = {
        'title': 'Test Note Title',
        'text': 'Test Note Test',
        'device_token': '123456'
    }

    client.post(reverse_url('Create-Note'),
                json=data, headers=headers).json()

    response = client.get(
        reverse_url('List-Notes'),
        headers=headers
    )
    content = response.json()

    assert response.status_code == 200
    assert content['count'] == 1
    assert len(content['results']) == 1


def test_category_notes_list(setup_tables):
    client = TestClient(app)
    client.post(reverse_url('Signup'), json=user)
    token = client.post(reverse_url('Login'), json=user).json()
    headers = {
        'Authorization': f"Token {token['token']}"
    }

    category_data = {
        'name': 'Test Category'
    }

    category = client.post(reverse_url('Create-Category'),
                           json=category_data, headers=headers).json()

    note_data = {
        'title': 'Test Note Title',
        'text': 'Test Note Test',
        'device_token': '123456',
        'category': category['id']
    }
    client.post(reverse_url('Create-Note'),
                json=note_data, headers=headers).json()

    response = client.get(
        reverse_url('Category-Notes', category=category['id']),
        headers=headers
    )
    content = response.json()

    assert response.status_code == 200
    assert content['count'] == 1
    assert len(content['results']) == 1
