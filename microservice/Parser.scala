package microservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import play.api.libs.json.{JsValue, Json}

import scala.util.Try

class WebServer(){
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    start
  }

  def start: Unit = {

    val route: Route =
      get {
        path("load") {
          complete {
            "Laden"
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8090)
    while (true) {
      Thread.sleep(100)
    }
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

  object Parser_Controller {
    def loadBoard(json: JsValue): Try[Array[Int]] = {
      Try {
        val boardArray = new Array[Int](14)

        val board = (json \ "gameboard" \ "board").get.toString()
        val jsonList: List[JsValue] = Json.parse(board).as[List[JsValue]]
        for (feld <- jsonList) {
          for (i: Int <- 0 to 13) {
            boardArray(i) = (feld \ i.toString).get.toString().toInt
            //                    controller.gameboard.gb(i) = (feld \ i.toString).get.toString().toInt
          }
        }
        boardArray
      }
    }
  }
