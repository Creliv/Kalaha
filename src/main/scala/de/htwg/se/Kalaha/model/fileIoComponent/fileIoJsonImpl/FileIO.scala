package de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl

import java.io.{File, PrintWriter}

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface
import de.htwg.se.Kalaha.model.fileIoComponent.FileIOInterface
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import play.api.libs.json.{JsNumber, JsString, JsValue, Json}

import scala.io.Source

class FileIO extends FileIOInterface {
  var round = 0
  //var boardArray = new Array[Int](14)
  //var boardArray2 = Gameboard(Array(14))
  //var value = 0

  override def load(controller: Controller): Unit = {
    val source1: String = Source.fromFile("D:\\board.json").getLines.mkString
    val json1: JsValue = Json.parse(source1)
    loadRound(json1, controller)
    loadBoard(json1, controller)
    controller.round = round
    controller.notifyObservers
  }

  def loadRound(json: JsValue, controller: Controller): Unit = {
    round = (json \ "gameboard" \ "round").get.toString().toInt
  }

  def loadBoard(json: JsValue, controller: Controller): Unit = {
    val board = (json \ "gameboard" \ "board").get.toString()
    val jsonList: List[JsValue] = Json.parse(board).as[List[JsValue]]

    for (feld <- jsonList) {
      print(feld)
      for (i: Int <- 0 to 13) {
        controller.gameboard.gb(i) = (feld \ i.toString).get.toString().toInt
        //print((feld \ i.toString).get.toString().toInt)
      }
    }
  }

  override def save(controller: Controller, board: Gameboard): Unit = {
    val pw = new PrintWriter(new File("D:\\board.json"))
    pw.write(Json.prettyPrint(toJson(controller)).toString)
    pw.close()
    print("Spielstand wurde als Json gespeichert.")
  }

  def toJson(controller: Controller):JsValue = {
    Json.obj(
      "gameboard" -> Json.obj(
        "round" -> JsNumber(round),
        "board" -> Json.arr(
          Json.obj(
            "0" -> controller.gameboard.gb(0),
            "1" -> controller.gameboard.gb(1),
            "2" -> controller.gameboard.gb(2),
            "3" -> controller.gameboard.gb(3),
            "4" -> controller.gameboard.gb(4),
            "5" -> controller.gameboard.gb(5),
            "6" -> controller.gameboard.gb(6),
            "7" -> controller.gameboard.gb(7),
            "8" -> controller.gameboard.gb(8),
            "9" -> controller.gameboard.gb(9),
            "10" -> controller.gameboard.gb(10),
            "11" -> controller.gameboard.gb(11),
            "12" -> controller.gameboard.gb(12),
            "13" -> controller.gameboard.gb(13),
          )
        )
      )
    )
  }
}