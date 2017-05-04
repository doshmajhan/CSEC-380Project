"""
    Program to recieve github push notification and then update
    the project on our two webservers

    3/31/17
    @author: Doshmajhan
"""
import json
import os
import paramiko
import flask
from flask import Flask
app = Flask(__name__)

def build(ip):
    print 'build'
    command = 'cd /var/www/CSEC-380Project; git pull'
    key_path = '/var/www/chaim.pem'
    ssh = paramiko.SSHClient()
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(ip, username='ubuntu', password='', key_filename=key_path)
    stdin, stdout, stderr = ssh.exec_command(command)
    print stdout.readlines()
    print stderr.readlines()
    ssh.close()


@app.route('/gitpush', methods=['GET', 'POST'])
def update():
    """
        Endpoint to recieve our push notification

    """
    data = flask.request.form
    try:
        data = dict(data)
        payload = json.loads(data['payload'][0])
        repository = payload['repository']['name']
        if repository == "CSEC-380Project":
            build('52.207.180.192')
            build('52.70.68.233')

    except Exception as e:
        print e
        resp = flask.Response("bad", status=500)
        return resp

    resp = flask.Response("good", status=200)
    return resp


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=6969)
