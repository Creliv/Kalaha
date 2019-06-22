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
    for (i <- 1 until 7) {
      gb(i) = amountStonesStart
    }
    for (i <- 8 until 14) {
      gb(i) = amountStonesStart
    }
  }

  def boardInit(gameboard: Array[Int]): Unit = {
    for (i <- 0 to 13) {
      gb(i) = gameboard(i)
    }
  }

  def setBoard(newBoard: Array[Int]): Unit = {
    for (i <- 0 to 13) {
      gb(i) = newBoard(i)
    }
  }

  override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }
}
