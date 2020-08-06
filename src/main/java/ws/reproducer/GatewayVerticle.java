package ws.reproducer;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

import java.util.concurrent.TimeUnit;

public class GatewayVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startFuture) {
        HttpServerOptions serverOptions = new HttpServerOptions()
                .setUseAlpn(true)
                .setCompressionSupported(true)
                .setIdleTimeout(7)
                .setIdleTimeoutUnit(TimeUnit.SECONDS);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, new SockJSHandlerOptions().setHeartbeatInterval(2000));
        sockJSHandler.socketHandler(new WebsocketHandler());

        Router router = Router.router(vertx);
        router.route("/MyServer/*").handler(sockJSHandler);

        vertx.createHttpServer(serverOptions)
                .requestHandler(router)
                .rxListen(config().getInteger("port", 8080))
                .subscribe(
                        server -> {
                            System.out.println("HTTP server started");
                            startFuture.complete();
                        }, error -> {
                            System.err.println("HTTP server failed to start : " + error);
                            startFuture.fail(error);
                        });
    }
}
