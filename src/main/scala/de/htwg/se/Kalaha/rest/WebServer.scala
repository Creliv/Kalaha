package de.htwg.se.Kalaha.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.view.tui.Tui

import scala.concurrent.Future
import scala.util._

class WebServer(tui: Tui) extends Controller {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  val route: Route =
    get {
      path("board") {
        complete {
          tui.showGameboard
//          "fisch"
        }
      }
    } ~
      path("help") {
        complete {
          tui.printHelp
//          "Kopf"
        }
      } ~
      path("move" / Segment) { s =>
        get {
          complete {
//            controller.moveTui(s.charAt(0).toString.toInt, s.charAt(0).toString.toInt)
            tui.startTurn(s.charAt(0).toString.toInt, s.charAt(1).toString.toInt) //return values needs to be string
//              "Yo"
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
