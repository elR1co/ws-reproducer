## Vert.x WS end handler problem reproducer

### Example with WS client https://dwst.github.io/

1. Start application in debug mode in IntelliJ :

MainClass : io.vertx.core.Launcher  
Program arguments : run ws.reproducer.GatewayVerticle

2. Go to https://dwst.github.io/

3. Connect with command : `/connect ws://localhost:8080/MyServer/websocket`

4. Wait 7 seconds

5. With Vert.x 3.8.5, the end handler is called and you should see the message in console : "Session {idSession} is closed.". With Vert.x 3.9.0 and higher, the end handler is not called.


### Example with Gatling WS request

1. Start reproducer as explained in first example

2. Run Gatling from IntelliJ by starting io.gatling.app.Gatling.Engine

3. Click on 'Enter' in console to run the Gatling simulation

4. Gatling sends a WS request to reproducer and close connection without sending WS close message

5. With Vert.x 3.8.5, the end handler is called and you should see the message in console : "Session {idSession} is closed.". With Vert.x 3.9.0 and higher, the end handler is not called.
