package de.htwg.se.Kalaha.model.gameboardController.GameboardImpl

import de.htwg.se.Kalaha.model.gameboardController.GameboardInterface

case class Gameboard(gb: Array[Int]) extends GameboardInterface {
  val SIZE = 14
  val stones = 6

  // deprecated function
  def boardInit(amountStonesStart: Int): Option[Unit] = {
    Some(setStones(amountStonesStart))
  }

  def boardInit(gameboard: Array[Int]): Option[Unit] = {
    Some(gameboard.copyToArray(gb))
  }

  def setBoard(newBoard: Array[Int]): Option[Unit] = {
    Some(newBoard.copyToArray(gb))
  }

  def setStones(amountStonesStart: Int): Unit = {
    val stones2 = amountStonesStart
    gb(0) = 0
    gb(7) = 0
    for (i <- 1 until 7) gb(i) = amountStonesStart
    for (i <- 8 until 14) gb(i) = amountStonesStart
  }

  /*override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }*/
}
