package microservice.controller

import play.api.libs.json.{JsValue, Json}

import scala.util.Try

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
