package de.htwg.se.Kalaha.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

class WebServer {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  def start: Unit = {
    val bindingFuture = Http().bindAndHandle(Routes.route, "localhost", 8080)
    while (true) {
      Thread.sleep(100)
    }
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
