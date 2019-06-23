package de.htwg.se.Kalaha.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller

class WebServer(controller: Controller) {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  val route: Route =
    get {
      path("board") {
        complete {
          controller.tui.showGameboard
        }
      }
    } ~
      path("help") {
        complete {
          controller.tui.printHelp
        }
      } ~
      path("move" / Segment) { s =>
        get {
          complete {
            controller.tui.startTurn(s.charAt(0).toString.toInt, s.charAt(0).toString.toInt) //return values needs to be string
          }
        }
      }

  def start: Unit = {
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    while (true) {
      Thread.sleep(100)
    }
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
