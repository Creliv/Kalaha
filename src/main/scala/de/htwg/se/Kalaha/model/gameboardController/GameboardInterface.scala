package de.htwg.se.Kalaha.model.gameboardController

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

trait GameboardInterface {

  def boardInit(amountStonesStart: Int): Option[Unit]

  def boardInit(gameboard: Array[Int]): Option[Unit]

  def setBoard(newBoard: Array[Int]): Option[Unit]

  def setStones(amountStonesStart: Int): Unit

  def doMove(input: Int, oldgb: Gameboard): Unit

  def setBoardPieces(oldgb: Gameboard, vBoard: Gameboard): Unit

  def collectEnemyStones(last: Int): Unit

  def checkExtra(last: Int): Unit

  def checkWin(): Unit

  def win(): Unit
}
