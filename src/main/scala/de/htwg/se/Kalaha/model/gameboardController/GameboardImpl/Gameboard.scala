package de.htwg.se.Kalaha.model.gameboardController.GameboardImpl

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.controller.controllerComponent.GameStatus.WON
import de.htwg.se.Kalaha.model.doa.slick.SlickImpl
import de.htwg.se.Kalaha.model.gameboardController.GameboardInterface

import scala.util.{Success, Failure}

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

  def clone(newBoard: Gameboard): Option[Unit] = {
    Some(newBoard.copy(gb))
  }

  def setStones(amountStonesStart: Int): Unit = {
    gb(0) = 0
    gb(7) = 0
//    for (i <- 1 until 7) gb(i) = amountStonesStart
    (1 until 7).foreach {i => gb(i) = amountStonesStart}
//    for (i <- 8 until 14) gb(i) = amountStonesStart
    (8 until 14).foreach {i => gb(i) = amountStonesStart}
  }

  def doMove(input: Int, oldgb: Gameboard): Unit = {
    var index = input
    var last = 0
    val turn = controller.round % 2
    gb.copyToArray(oldgb.gb)
    val countStonesInMuld: Int = gb(index)
    gb(index) = 0
//    for (i <- 1 until countStonesInMuld + 1)
    (1 until countStonesInMuld+1).foreach { i =>
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
    checkExtra(last)
    controller.round += 1
  }

  def setBoardPieces(oldgb: Gameboard, vBoard: Gameboard): Unit = {
    gb.copyToArray(vBoard.gb)
    oldgb.gb.copyToArray(gb)
    vBoard.gb.copyToArray(oldgb.gb)
  }

  def collectEnemyStones(last: Int): Unit = {
    var own = false
    val turn = controller.round % 2
    if ((1 <= last) && (last <= 6) && turn == 0) own = true
    if ((8 <= last) && (last <= 13) && turn == 1) own = true
    if (own) {
      val idx = 14 - last
      if (turn == 0) {
        gb(p1) += gb(idx)
        gb(p1) += gb(last)
        gb(idx) = 0
        gb(last) = 0
      } else {
        gb(p2) += gb(idx)
        gb(p2) += gb(last)
        gb(idx) = 0
        gb(last) = 0
      }
    }
  }

  def checkExtra(last: Int): Unit = {
    val turn = controller.round % 2
    if ((turn == 1 && last == 0) || (turn == 0 && last == 7)) controller.round -= 1
    if (gb(last) == 1) collectEnemyStones(last)
    controller.notifyObservers
  }

  def checkWin(): Unit = {
    var x: Int = 0
//    for (i <- 1 until 6 + 1) x += gb(i)
    (1 until 6+1).foreach {i => x += gb(i)}
    var y: Int = 0
//    for (i <- 1 until 6 + 1) y += gb(i + 7)
    (1 until 6+1).foreach {i => y += gb(i+7)}
    if (x == 0 || y == 0) win()
  }

  def win(): Unit = {
    var x: Int = 0
    for (i <- 1 until 6 + 1) {
      x += gb(i)
      gb(i) = 0
    }
    var y: Int = 0
    for (i <- 1 until 6 + 1) {
      y += gb(i + p1)
      gb(i) = 0
    }
    gb(p1) += x
    gb(p2) += y
    match {
      case a if gb(p1) > gb(p2) =>
        //print("P1: " + board.gameboard(p1) + " P2: " + board.gameboard(2) + "\n")
        print("WIN PLAYER 1\n")
        controller.p1win = true
      case a if gb(p2) > gb(p1) =>
        //print("P1: " + board.gameboard(p1) + " P2: " + board.gameboard(p2) + "\n")
        print("WIN PLAYER 2\n")
        controller.p2win = true
      case _ =>
        print("TIE\n")
        controller.p2win = true
        controller.p1win = true
    }
    controller.gameStatus = WON
    //notifyObservers
  }

  def loadSlick(id: Int) = {
    import scala.concurrent.ExecutionContext.Implicits.global
    SlickImpl.findById(id).onComplete {
      case Success(boardValues) => {
        val array = boardValues._4.split(";").map(_.toInt)
        boardInit(array)
        controller.round = boardValues._3
        controller.amountStones = boardValues._2
        controller.notifyObservers()
      }
      case Failure(e) => println("Error: Failed to load game id" + e)
    }
  }

  def saveSlick(id: Int) = {
    SlickImpl.insert(id, controller.amountStones, controller.round, gb.mkString(";"))
  }


  /*override def toString: String = {
    var s: String = ""
    for (i <- gb.indices)
      s += gb(i)
    s
  }*/
}
