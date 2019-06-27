package de.htwg.se.Kalaha.model.gameboardController.GameboardImpl

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.gameboardController.GameboardInterface

case class Gameboard(gb: Array[Int], controller: Controller) extends GameboardInterface {
  val SIZE = 14
  val stones = 6

  val p1 = 7
  val p2 = 0

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

  //TODO function to clone gameboard
  def clone(newBoard: Gameboard): Option[Unit] = {
    Some(newBoard.copy(gb))
  }

  def setStones(amountStonesStart: Int): Unit = {
    val stones2 = amountStonesStart
    gb(0) = 0
    gb(7) = 0
    for (i <- 1 until 7) gb(i) = amountStonesStart
    for (i <- 8 until 14) gb(i) = amountStonesStart
  }

  def doMove(input: Int, oldgb: Gameboard): Unit = {
    var index = input
    var last = 0
    var turn = controller.round % 2
    //TODO fix save
    gb.copyToArray(oldgb.gb)
    //TODO check if mulde is empty.
    val countStonesInMuld: Int = gb(index)
    gb(index) = 0
    for (i <- 1 until countStonesInMuld + 1) {
      if ((turn == 0 && (index + i) % 14 == 0) || (turn == 1 && (index + i) % 14 == p1)) {
        if (index + i >= gb.size) {
          val y: Int = (index + i - gb.size) % 14
          gb(y + 1) += 1
          index += 1
        } else {
          gb(index + i) += 1
        }
      } else {
        if (index + i >= gb.size) {
          val y: Int = (index + i - gb.size) % 14
          gb(y) += 1
        } else {
          gb(index + i) += 1
        }
      }
      if (i == countStonesInMuld) last = (index + i) % 14
    }
    controller.undone = false
    controller.checkExtra(last)
    controller.round += 1
  }

  /*override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }*/
}
