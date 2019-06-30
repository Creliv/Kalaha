package micros

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object Parser {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    start
  }

  def start: Unit = {

    val route: Route =
    //path("load") {
      post {
        entity(as[String]) { pathToFile =>
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, Parser_Controller.loadBoard(pathToFile))))
        }
      }
    //         complete {
    //           "Laden"
    //        }
    //}

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8090)
    while (true) {
      Thread.sleep(100)
    }
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

  def test(x: String): String = {
    x
  }
}