package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.Kalaha.controller.controllerComponent.GameStatus._
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl.FileIO
import de.htwg.se.Kalaha.util.{Observable, UndoManagerImpl}
import de.htwg.se.Kalaha.view.gui.Gui
import de.htwg.se.Kalaha.view.tui.Tui

import scala.swing.Publisher
import scala.util._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

class Controller() extends Observable with ControllerInterface with Publisher{
  val stones: Int = 6
  val boardArray = Array(0, stones, stones, stones, stones, stones, stones, 0, stones, stones, stones, stones, stones, stones)
  val gameboard = new Gameboard(new Array[Int](14), this)
  val oldgb = new Gameboard(new Array[Int](14), this)
  val vBoard = new Gameboard(new Array[Int](14), this)

  var gameStatus: GameStatus = IDLE

  var amountStones = 0
  var undone = false
  var p1win, p2win = false
  val p1 = 7
  val p2 = 0
  private val undoManager = new UndoManagerImpl(this)
  val fileIO = new FileIO
  var round = 0
  var playerTurn1 = true
  var playerTurn2 = false

  updateStones(6)
  gameboard.boardInit(boardArray).getOrElse(new Exception("Error: Could not initialize board!"))
  gameStatus = NEW

  def updateStones(x: Int): Unit = {
    amountStones = x
  }

  //TODO implement playerTurn and emptyField here (from gui/tui)
  def moveGui(inputX: Int, inputY: Int): Future[Unit] = {
    Future {

      var index = inputY
      var turn = round % 2

      print(inputX)
      // turn >> playerTurn: Boolean
      if (inputX == 0 && turn == 1) {
        index = 13 - inputY
        gameboard.doMove(index, oldgb)
      } else if (inputX == 1 && turn == 0) {
        index = inputY + 1
        gameboard.doMove(index, oldgb)
      } else {
        Failure(throw new Exception("Error: not your turn!"))
      }
    }
  }

  def moveTui(inputX: Int, inputY: Int): Future[Unit] = {
    Future {
      var index = inputY
      //var index = 0
      var turn = round % 2

      print(inputX)
      // turn >> playerTurn: Boolean
      if (inputX - 1== 0 && turn == 1) {
        index = inputY + 8
        gameboard.doMove(index, oldgb)
      } else if (inputX + 1== 1 && turn == 0) {
        index = inputY + 1
        gameboard.doMove(index, oldgb)
      } else {
        Failure(throw new Exception("Error: not your turn!"))
      }
    }
  }

//  def doMove(input: Int): Unit = {
//    var index = input
//    var last = 0
//    var turn = round % 2
//    for (i <- 0 to 13) {
//      oldgb.gb(i) = gameboard.gb(i)
//    }
//    //TODO check if mulde is empty.
//    val countStonesInMuld: Int = gameboard.gb(index)
//    gameboard.gb(index) = 0
//    for (i <- 1 until countStonesInMuld + 1) {
//      if ((turn == 0 && (index + i) % 14 == 0) || (turn == 1 && (index + i) % 14 == p1)) {
//        if (index + i >= gameboard.gb.size) {
//          val y: Int = (index + i - gameboard.gb.size) % 14
//          gameboard.gb(y + 1) += 1
//          index += 1
//        } else {
//          gameboard.gb(index + i) += 1
//        }
//      } else {
//        if (index + i >= gameboard.gb.size) {
//          val y: Int = (index + i - gameboard.gb.size) % 14
//          gameboard.gb(y) += 1
//        } else {
//          gameboard.gb(index + i) += 1
//        }
//      }
//      if (i == countStonesInMuld) last = (index + i) % 14
//    }
//    undone = false
//    checkExtra(last)
//    this.round += 1
//  }

  def checkPlayerTurn: Boolean = {
    if (round % 2 == 0) {
      true
    } else {
      false
    }
  }

  def collectEnemyStones(last: Int): Unit = {
    var own = false
    if ((1 <= last) && (last <= 6) && round % 2 == 0) own = true
    if ((8 <= last) && (last <= 13) && round % 2 == 1) own = true
    //print("\nown= " + own)
    if (own) {
      val idx = 14 - last
      if (round % 2 == 0) {
        gameboard.gb(p1) += gameboard.gb(idx)
        gameboard.gb(p1) += gameboard.gb(last)
        gameboard.gb(idx) = 0
        gameboard.gb(last) = 0
      } else {
        gameboard.gb(p2) += gameboard.gb(idx)
        gameboard.gb(p2) += gameboard.gb(last)
        gameboard.gb(idx) = 0
        gameboard.gb(last) = 0
      }
    }
  }

  def checkExtra(last: Int): Unit = {
    if ((round % 2 == 1 && last == 0) || (round % 2 == 0 && last == 7)) round -= 1
    if (gameboard.gb(last) == 1) collectEnemyStones(last)
    notifyObservers
  }

  def undo(): Try[Unit] = Try {
    if (undone) {
      Failure(throw new IllegalArgumentException("Es ist nur möglich einen Zug rückgängig zu machen1"))
    } else {
      gameStatus = UNDO
      undoManager.undoMove.get
      round -= 1
      undone = true
      notifyObservers
    }
  }

  def redo(): Try[Unit] = Try {
    if(!undone) {
      Failure(throw new IllegalArgumentException("Es ist nur möglich einen Zug vorwärts zu machen1"))
    } else {
      gameStatus = REDO
      undoManager.redoMove.get
      round += 1
      undone = false
      notifyObservers
    }
  }

  def reset(): Unit = {
    gameboard.boardInit(amountStones) match {
      case Some(_) => {
        println("success: reset board")
        gameStatus = RESET
      }
      case None => println("Error: Could not initialize board1!")
    }
    round = 0
    notifyObservers
  }

  def checkWin(): Unit = {
    var x: Int = 0
    for (i <- 1 until 6 + 1) x += gameboard.gb(i)
    var y: Int = 0
    for (i <- 1 until 6 + 1) y += gameboard.gb(i + 7)
    if (x == 0 || y == 0) win()
  }

  def win(): Unit = {
    var x: Int = 0
    for (i <- 1 until 6 + 1) {
      x += gameboard.gb(i)
      gameboard.gb(i) = 0
    }
    var y: Int = 0
    for (i <- 1 until 6 + 1) {
      y += gameboard.gb(i + p1)
      gameboard.gb(i) = 0
    }
    gameboard.gb(p1) += x
    gameboard.gb(p2) += y
    match {
      case a if gameboard.gb(p1) > gameboard.gb(p2) =>
        //print("P1: " + board.gameboard(p1) + " P2: " + board.gameboard(2) + "\n")
        print("WIN PLAYER 1\n")
        p1win = true
      case a if gameboard.gb(p2) > gameboard.gb(p1) =>
        //print("P1: " + board.gameboard(p1) + " P2: " + board.gameboard(p2) + "\n")
        print("WIN PLAYER 2\n")
        p2win = true
      case _ =>
        print("TIE\n")
        p2win = true
        p1win = true
    }
    gameStatus = WON
    //notifyObservers
  }

  def exit(): Unit = sys.exit(0)

  def save: Unit = Try[Unit] {
    fileIO.save(this) match {
      case Success(_) => println("Successfully written to Json-File!")
      case Failure(e) => println(e)
    }
  }
  def load: Unit = Try[Unit] {
    fileIO.load(this) match {
      case Success(v) => println("Successfully loaded gamestate from Json-File!")
      case Failure(e) => println("Error: " + e)
    }
  }
  //TODO doesnt get updated properly
  def statusText: String = GameStatus.message(gameStatus)
}
