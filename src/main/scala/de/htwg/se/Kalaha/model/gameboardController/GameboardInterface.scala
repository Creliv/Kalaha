package de.htwg.se.Kalaha.model.gameboardController

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

import scala.concurrent.Future

trait GameboardInterface {

  def boardInit(amountStonesStart: Int): Option[Array[Int]]

  def boardInit(gameboard: Array[Int]): Option[Array[Int]]

  def setBoard(newBoard: Array[Int]): Option[Array[Int]]

  def setStones(amountStonesStart: Int): Boolean

  def doMove(input: Int, oldgb: Gameboard): Boolean

  def setBoardPieces(oldgb: Gameboard, vBoard: Gameboard): Boolean

  def collectEnemyStones(last: Int): Int

  def checkExtra(last: Int): Boolean

  def checkWin(): Boolean

  def win(): Boolean
}
