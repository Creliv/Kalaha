package de.htwg.se.Kalaha.model.gameboardController.GameboardImpl

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.gameboardController.GameboardInterface
import play.api.libs.json._

case class Gameboard(gb: Array[Int]) extends GameboardInterface {
  //var round = 0
  val SIZE = 14
  val stones = 6
  //val startboard: Array[Int] = Array[Int](0, stones, stones, stones, stones, stones, stones, 0, stones, stones, stones, stones, stones, stones)
  //var gameboard = new Array[Int](SIZE)
  //var oldgb = new Array[Int](SIZE)

  def boardInit(amountStonesStart: Int): Unit = {
    val stones2 = amountStonesStart
    for (i <- 1 until SIZE) {
      //print(gameboard(i))
      if (gb(i) != 0) gb(i) = amountStonesStart
    }
  }

  def boardInit(): Unit = ???

  def setBoard(newBoard: Array[Int]): Unit = ???

  override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }
}
