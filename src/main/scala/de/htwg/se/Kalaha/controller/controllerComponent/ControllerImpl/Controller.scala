package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.controller.controllerComponent.{ControllerInterface, GameStatus}
import de.htwg.se.Kalaha.controller.controllerComponent.GameStatus._
import de.htwg.se.Kalaha.model.doa.slick.SlickImpl
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl.FileIO
import de.htwg.se.Kalaha.util.{Observable, UndoManagerImpl}

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

  var playerTurn1 = true
  var playerTurn2 = false
  var round = 0

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

  def exit(): Unit = sys.exit(0)

  def save(path: String): Unit = Try[Unit] {
    fileIO.save(this, path) match {
      case Success(_) => println("Successfully written to Json-File!")
      case Failure(e) => println(e)
    }
  }
  def load(path: String): Unit = Try[Unit] {
    fileIO.load(this, path) match {
      case Success(v) => println("Successfully loaded gamestate from Json-File!")
      case Failure(e) => println("Error: " + e)
    }
  }
  //TODO doesnt get updated properly
  def statusText: String = GameStatus.message(gameStatus)

  def slicktestLoad(id: Int) = {
//    gameboard.loadSlick(id).onComplete {
//      case Success(board) => {
//
//      }
//    }
    gameboard.loadSlick(id)
  }

  def slicktestSave(id: Int) = {
    gameboard.saveSlick(id)
  }
}
