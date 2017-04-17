"""
	Test cases for authentication aspects of the applications
        4/17/2017
        Cameron Clark
"""

import requests
AUTH_API = 'http://54.208.87.70/'

def register_users():
    global AUTH_API
    users = ['cam', 'hayden', 'dosh']
    passwords = ['password1', 'password2', 'password3']
    for user, password in zip(users, passwords):
        r = requests.post("{}register".format(AUTH_API), data=data)
        if r.status_code == '200':
            print True
        else:
            print False

def delete_users():
    global AUTH_API
    users = ['cam', 'hayden', 'dosh']
    passwords = ['password1', 'password2', 'password3']
    for user, password in zip(users, passwords):
        r = requests.post("{}delete".format(AUTH_API), data=data)
        if r.status_code == '200':
            print True
        else:
            print False

def is_authenticated():
    global AUTH_API
    users = ['cam', 'hayden', 'dosh']
    passwords = ['password1', 'password2', 'password3']
    for user, password in zip(users, passwords):
        r = requests.post("{}isAuthenticated".format(AUTH_API), data=data)
        if r.status_code == '200':
            print True
        else:
            print False

def func(num):
    return num + 1

def test_answer():
    assert func(4) == 5

