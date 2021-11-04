# spring-boot-28525
Demonstration of spring-boot bug report #28525.

Clone the repo locally and then:

* Run   "mvn clean package"

* Run   "java -jar target/jetty-websocket-demo-0.0.1-SNAPSHOT.jar"

* Use Postman v8.5.0 or later to test it. (See here for instructions on testing a websocket with Postman: https://learning.postman.com/docs/sending-requests/supported-api-frameworks/websocket/ )


To see good, working behavior, try to open a WebSocket connection to ws://localhost:8080/websocket/good. You will be able to connect, send a message to the server, receive a reply, and disconnect.

To see the error, try to open a WebSocket connection to ws://localhost:8080/websocket/bad. You will not be able to connect, and if you check the console you will see log messages that show that servletContext is null and that the handshake has encountered a NPE.

