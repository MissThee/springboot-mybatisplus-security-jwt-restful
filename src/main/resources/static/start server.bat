title test port:8096
java -jar ../../../../target/demo-0.0.1-SNAPSHOT.jar --server.host=localhost --server.port=8096 --socketio.port=8097 --custom-config.upload.path=C:/Project-springtest/file/ --logging.level.server=debug
echo PRESS ANY KEY TO EXIT
pause & exit
::-Dfile.encoding=utf-8