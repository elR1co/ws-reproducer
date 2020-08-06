package ws.reproducer

object Configuration {

  val host = System.getProperty("gatling.perf.host", "localhost")
  val port = Integer.getInteger("gatling.perf.port", 8080)
  val isSsl = java.lang.Boolean.valueOf(System.getProperty("gatling.perf.sslEnabled", "false"))
  val httpBaseUrl = "http" + (if (isSsl) "s" else "") + "://" + host + ":" + port
  val wsBaseUrl = "ws" + (if (isSsl) "s" else "") + "://" + host + ":" + port
}
