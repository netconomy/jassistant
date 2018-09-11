from fabric.api import env, put, run, local

env.hosts = ['root@jassistant.local.netconomy.net']
#env.hosts = ['root@10.98.138.60']

def deploy():
    img = 'jassistant.img'
    remote_path = '/var/tmp/{}'.format(img)
    local('gradle clean build')
    local('docker build --rm -t jassistant .')
    local('docker save -o {} jassistant:latest'.format(img))
    put(local_path=img, remote_path=remote_path)
    put(local_path='JiraAssistant.properties', remote_path='/etc/jassistant/JiraAssistant.properties')
    run('systemctl stop jassistant.service', warn_only=True)
    run('docker rmi jassistant', warn_only=True)
    run('docker load -i {}'.format(remote_path))
    run('systemctl start jassistant.service')


def updateConfig():
    run('systemctl stop jassistant.service')
    put(local_path='JiraAssistant.properties', remote_path='/etc/jassistant/JiraAssistant.properties')
    run('systemctl start jassistant.service')


def localRun():
    local('gradle clean build')
    local('docker build -t jassistant .')
    local('docker run --rm -v C:\Development\JiraAssistantRestServices\JiraAssistant.properties:/usr/local/tomcat/conf/JiraAssistant.properties -p 8080:8080 jassistant')
	

def localDebug():
    local('gradle clean build')
    local('docker build -t jassistant .')
    local('docker run --rm -v C:\Development\JiraAssistantRestServices\JiraAssistant.properties:/usr/local/tomcat/conf/JiraAssistant.properties -p 8080:8080 -p 8000:8000 -e JPDA_ADDRESS=8000 -e JPDA_TRANSPORT=dt_socket jassistant catalina.sh jpda run')
