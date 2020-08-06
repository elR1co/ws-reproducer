package ws.reproducer;

import io.reactivex.Flowable;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSSocket;

import java.util.UUID;

public class WebsocketHandler implements Handler<SockJSSocket> {

    @Override
    public void handle(SockJSSocket sockJSSocket) {
        Session newSession = new Session();
        System.out.println("Created new session with id " + newSession.id());

        Runnable clearSessionData = () -> {
            newSession.clear();
            System.out.println("Session " + newSession.id() + " is closed.");
        };

        sockJSSocket
                .toFlowable()
                .map(Buffer::toJsonObject)
                // onMessage
                .flatMap(message -> {
                    System.out.println("Received request : " + message);
                    String response = UUID.randomUUID().toString();
                    newSession.addMessage(response);
                    return Flowable.just(new JsonObject().put("response", "Hello " + response));
                })
                .subscribe(
                        response -> {
                            System.out.println("Received response : " + response);
                            sockJSSocket.write(Buffer.newInstance(response.toBuffer()));
                        },
                        // onError
                        error -> {
                            System.err.println("Fatal error for session with id " + newSession.id() + " : " + error);
                            clearSessionData.run();
                            sockJSSocket.close();
                        },
                        // onClose
                        clearSessionData::run
                );
    }
}
