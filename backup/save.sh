docker save $(docker images -q) -o backup.tar
tar cf jenkins.tar jenkins/
tar cf gogs.tar gogs/