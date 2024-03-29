"""
	Test cases for authentication aspects of the applications
        4/17/2017
        Cameron Clark
"""

import requests

AUTH_API = 'http://54.208.87.70:8181/'
#AUTH_USERS = ['cam', 'hayden', 'dosh']
#AUTH_PASSWORDS = ['password1', 'password2', 'password3']
AUTH_USERS = ['cpc1007']
AUTH_PASSWORDS = ['Itsthedosh23$']
BAD_USERS = ['ted', 'bob']
BAD_PASSWORDS = ['changeme1', 'changeme2']


class TestAuth:
    """
        Collection of functions to test our J2EE authentication
    """
    def test_register(self):
        """
            Tries to register a user against our authentication service

            Expects a 200 response for each request
        """
        for user, password in zip(AUTH_USERS, AUTH_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}register".format(AUTH_API), data=data)
            assert r.status_code == '200'


    def test_delete(self):
        """
            Tries to delete a user that exists in our authentication service

            Expects a 200 for existing and 404 for non-existing users
        """
        for user, password in zip(AUTH_USERS, AUTH_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}delete".format(AUTH_API), data=data)
            assert r.status_code == '200'

        for user, password in zip(BAD_USERS, BAD_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}delete".format(AUTH_API), data=data)
            assert r.status_code == '404'


    def test_login(self):
        """
            Tries to log in as a user

            Expects a 200 for existing and 403 for not exisitng
        """
        for user, password in zip(AUTH_USERS, AUTH_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}login".format(AUTH_API), data=data)
            assert r.status_code == '200'

        for user, password in zip(BAD_USERS, BAD_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}login".format(AUTH_API), data=data)
            assert r.status_code == '403'


    def is_authenticated(self):
        """
            Checks if a given user is authenticated

            Expects a 200 if its authenticated, 403 if not
        """
        for user, password in zip(AUTH_USERS, AUTH_PASSWORDS):
            data = "user={}&pass={}".format(user, password)
            r = requests.post("{}login".format(AUTH_API), data=data)
            print r.text
            #r = requests.post("{}isAuthenticated".format(AUTH_API), data=data)
            #assert r.status_code == '200'

        """for user, password in zip(BAD_USERS, BAD_PASSWORDS):
            r = requests.post("{}isAuthenticated".format(AUTH_API), data=data)
            assert r.status_code == '403'
        """

def func(num):
    return num + 1

def test_answer():
    assert func(4) == 5

if __name__ == '__main__':
    auth = TestAuth()
    auth.is_authenticated()
