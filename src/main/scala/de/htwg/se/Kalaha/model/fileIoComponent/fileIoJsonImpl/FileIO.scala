package de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl

import java.io.{File, PrintWriter}

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.fileIoComponent.FileIOInterface
import play.api.libs.json.{JsNumber, JsValue, Json}

import scala.io.Source
import scala.util._

class FileIO extends FileIOInterface {
  var round = 0

  override def load(controller: Controller, path: String): Try[Unit] = {
    Try {
      val source1: String = Source.fromFile(path).getLines.mkString
      val json1: JsValue = Json.parse(source1)
      loadRound(json1, controller) match {
        case Success(v) => controller.round = v
        case Failure(e) => println("Error: Could not parse Json-File for <round>" + e)
      }
      loadStones(json1, controller) match {
        case Success(v) => controller.amountStones = v
        case Failure(e) => println("Error: Could not parse Json-File for <round>" + e)
      }
      loadBoard(json1, controller) match {
        case Success(v) => controller.gameboard.boardInit(v)
        case Failure(e) => println("Error: Could not parse Json-File for <round>" + e)
      }
      controller.notifyObservers
    }
  }

  def loadRound(json: JsValue, controller: Controller): Try[Int] = {
    Try {
      (json \ "gameboard" \ "round").get.toString().toInt
    }
  }

  def loadStones(json:JsValue, controller: Controller): Try[Int] = {
    Try {
      (json \ "gameboard" \ "amountstones").get.toString().toInt
    }
  }

  def loadBoard(json: JsValue, controller: Controller): Try[Array[Int]] = {
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

  override def save(controller: Controller, path: String): Try[Unit] = {
    Try {
      val pw = new PrintWriter(new File(path))
      pw.write(Json.prettyPrint(toJson(controller)).toString)
      pw.close()
    }
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