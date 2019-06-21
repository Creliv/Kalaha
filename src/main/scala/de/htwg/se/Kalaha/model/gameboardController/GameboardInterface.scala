package de.htwg.se.Kalaha.model.gameboardController

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

trait GameboardInterface {

  def boardInit(amountStonesStart: Int): Unit

  def boardInit(gameboard: Array[Int]): Unit

  def setBoard(newBoard: Array[Int]): Unit

  override def toString: String

}
