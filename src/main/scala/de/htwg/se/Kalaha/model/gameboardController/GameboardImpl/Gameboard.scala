package de.htwg.se.Kalaha.model.gameboardController.GameboardImpl

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.gameboardController.GameboardInterface

case class Gameboard(gb: Array[Int]) extends GameboardInterface {
  val SIZE = 14
  val stones = 6

  def boardInit(amountStonesStart: Int): Unit = {
    val stones2 = amountStonesStart
    gb(0) = 0
    gb(7) = 0
    for (i <- 2 until 7) {
      gb(i) = amountStonesStart
    }
    for (i <- 8 until SIZE) {
      gb(i) = amountStonesStart
    }
  }

  def boardInit(): Unit = ???

  //def setBoard(newBoard: Array[Int]): Unit = ???

  override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }
}
