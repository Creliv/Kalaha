package de.htwg.se.Kalaha.model.gameboardController

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

trait GameboardInterface {

  def boardInit(amountStonesStart: Int): Option[Unit]

  def boardInit(gameboard: Array[Int]): Option[Unit]

  def setBoard(newBoard: Array[Int]): Option[Unit]

  override def toString: String

}
