package de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.core.JsonParseException
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.fileIoComponent.FileIOInterface
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import play.api.libs.json.{JsNumber, JsValue, Json}

import scala.io.Source
import scala.util._

class FileIO extends FileIOInterface {
  var round = 0

  override def load(controller: Controller): Try[Unit] = Try {
    val source1: String = Source.fromFile("D:\\board.json").getLines.mkString
    val json1: JsValue = Json.parse(source1)
    loadRound(json1, controller) match {
      case Some(v) => controller.round = v
      case None => println("Error: Could not parse Json-File for <round>")
    }
    loadStones(json1, controller) match {
      case Some(v) => controller.amountStones = v
      case None => println("Error: Could not parse Json-File for <round>")
    }
    loadBoard(json1, controller) /*match {
      case Some(v) => controller.gameboard.setBoard(v)
      case None => println("Error: Could not parse Json-File for <gameboard>")
    }*/
    controller.notifyObservers
  }


  def loadRound(json: JsValue, controller: Controller): Option[Int] = {
    try {
      Some((json \ "gameboard" \ "round").get.toString().toInt)
    } catch {
      case e: JsonParseException => None
    }
  }

  def loadStones(json:JsValue, controller: Controller): Option[Int] = {
    try {
      Some((json \ "gameboard" \ "amountstones").get.toString().toInt)
    } catch {
      case e: JsonParseException => None
    }
  }

  /*def loadBoard(json: JsValue, controller: Controller): Option[Array[Int]] = {

    val board = (json \ "gameboard" \ "board").get.toString()
    val jsonList: List[JsValue] = Json.parse(board).as[List[JsValue]]

    for (feld <- jsonList) {
      for (i: Int <- 0 to 13) {
        arr(i) = (feld \ i.toString).get.toString().toInt
      }
    }
    Some(arr)
    None
  }*/

  def loadBoard(json: JsValue, controller: Controller): Unit = {
    val board = (json \ "gameboard" \ "board").get.toString()
    val jsonList: List[JsValue] = Json.parse(board).as[List[JsValue]]
    for (feld <- jsonList) {
      for (i: Int <- 0 to 13) {
        controller.gameboard.gb(i) = (feld \ i.toString).get.toString().toInt
      }
    }
  }

  override def save(controller: Controller, board: Gameboard): Try[Unit] = Try {
    val pw = new PrintWriter(new File("D:\\board.json"))
    pw.write(Json.prettyPrint(toJson(controller)).toString)
    pw.close()
  }

  def toJson(controller: Controller): JsValue = {
    Json.obj(
      "gameboard" -> Json.obj(
        "round" -> JsNumber(controller.round),
        "amountstones" -> JsNumber(controller.amountStones),
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