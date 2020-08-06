package ws.reproducer

import java.util.concurrent.TimeUnit

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class WebsocketSimulation extends Simulation {

  val httpProtocol = http.wsBaseUrl(Configuration.wsBaseUrl).wsReconnect.wsMaxReconnects(1)
    .header("Sec-WebSocket-Version", "13")
    .header("Sec-WebSocket-Extensions" , "permessage-deflate client_max_window_bits")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
  val request = """{"request":"hello"}"""
  val responseCheck = ws.checkBinaryMessage("checkAckResponse").check(bodyBytes.transform(_.length).gt(1))

  val scn = scenario("Ws Check end handler")
    .exec(ws("Connect WS").connect("/MyServer/websocket")
      .onConnected(
        exec(ws("Send request")
          .sendBytes(request.getBytes("UTF-8"))
          .await(30 seconds)(responseCheck)
        )
        .pause("5", "3000", TimeUnit.MILLISECONDS)
      ))

  setUp(
    scn.inject(
      atOnceUsers(1)
    )
      .protocols(httpProtocol))
    .assertions(
      //global.responseTime.max.lt(30000),
      forAll.failedRequests.count.is(0)
    )
}
