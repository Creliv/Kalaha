package micros

import java.io.FileNotFoundException

import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object Parser_Controller {

  @throws(classOf[FileNotFoundException])
  def loadBoard(path: String): String = {
      println("Hallo")
      val source1: String = Source.fromFile(path).getLines.mkString
      val json: JsValue = Json.parse(source1)
      val boardArray = new Array[Int](14)
      println("source: " + source1)

      val board = (json \ "gameboard" \ "board").get.toString()
      val jsonList: List[JsValue] = Json.parse(board).as[List[JsValue]]
      for (feld <- jsonList) {
        for (i: Int <- 0 to 13) {
          boardArray(i) = (feld \ i.toString).get.toString().toInt
          //                    controller.gameboard.gb(i) = (feld \ i.toString).get.toString().toInt
        }
      }
      boardArray.mkString(",")
    }

}