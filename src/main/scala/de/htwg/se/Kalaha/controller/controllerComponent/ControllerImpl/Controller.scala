package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.model.fileIoComponent.fileIoJsonImpl.FileIO
import de.htwg.se.Kalaha.util.{Observable, UndoManager}
import de.htwg.se.Kalaha.view.gui.Gui
import de.htwg.se.Kalaha.view.tui.Tui

class Controller() extends Observable with ControllerInterface {
  val stones: Int = 6
  val board = new Gameboard(Array(0, stones, stones, stones, stones, stones, stones, 0, stones, stones, stones, stones, stones, stones))
  val startboard = new Gameboard(Array(0, stones, stones, stones, stones, stones, stones, 0, stones, stones, stones, stones, stones, stones))
  var gameboard = new Gameboard(Array(14))
  val oldgb = new Gameboard(Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0))
  val undostack = new Array[Int](14)
  val vBoard = new Gameboard(Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0))
  //var vboard = new
  var amountStones = 0
  var undone = false
  var p1win = false
  var p2win = false
  val p1 = 7
  val p2 = 0
  var undoManager: UndoManager = _
  val fileIO = new FileIO
  var round = 0

  //val injector = Guice.createInjector(new GameboardModule)
  //val fileIo = injector.instance[FileIOInterface]

  def controllerInit(amountStonesStart: Int): Unit = {
    amountStones = amountStonesStart
    gameboard.boardInit(amountStonesStart)
    print(board, "---------------------------------")
    // notifyObservers
  }

  def controllerInit(): Unit = {
    updateStones(6)
    println(gameboard)
    gameboard = board.copy()
    println(gameboard)
    //gameboard.gb foreach println
    //val tui = new Tui(this)
    val gui = new Gui(this)
    //tui.startGame()
  }

  def updateStones(x: Int): Unit = {
    amountStones = x
  }

  def move(inputIndex: Int): Unit = {
    var index = inputIndex
    var last = 0
    //print("index = " + index + "\n")
    val turn = round % 2
    //print("Turn = " + turn + "\n")
    println("TESTTESTTEST")
    for (i <- 0 to 13) {
      oldgb.gb(i) = gameboard.gb(i)
    }
    val countStonesInMuld: Int = gameboard.gb(index)
    //print("Balls = " + countStonesInMuld + "\n")
    gameboard.gb(index) = 0
    for (i <- 1 until countStonesInMuld + 1) {
      if ((turn == 0 && (index + i) % 14 == 0) || (turn == 1 && (index + i) % 14 == p1)) {
        //print("turn: " + round % 2 + " i = " + (index + i) + " x = " + countStonesInMuld + " skip\n")
        //check if last hole > gameboard
        if (index + i >= gameboard.gb.length) {
          val y: Int = (index + i - gameboard.gb.length) % 14
          gameboard.gb(y + 1) += 1
          index += 1
        } else {
          gameboard.gb(index + i) += 1
        }
      } else {
        if (index + i >= gameboard.gb.length) {
          val y: Int = (index + i - gameboard.gb.length) % 14
          gameboard.gb(y) += 1
        } else {
          gameboard.gb(index + i) += 1
        }
      }
      if (i == countStonesInMuld) {
        last = (index + i) % 14
      }
    }

    undone = false
    notifyObservers
    checkExtra(last)

    round += 1
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
    //checkWin()
    //print("checkExtra!\n")
    if ((round % 2 == 1 && last == 0) || (round % 2 == 0 && last == 7)) {
      //print("New Turn")
      //tui.startTurn()
      //notifyObservers

      round -= 1
      notifyObservers
    }
    if (gameboard.gb(last) == 1) {
      collectEnemyStones(last)
      notifyObservers
    }

  }

  //TODO fix undo
  def undo(): Unit = {
    try{
      if (undone) {
        throw new IllegalArgumentException("Es ist nur möglich einen Zug rückgängig zu machen")
      } else {
        for (i <- 0 to 13) {
          vBoard.gb(i) = gameboard.gb(i)
          gameboard.gb(i) = oldgb.gb(i)
          oldgb.gb(i) = vBoard.gb(i)
        }
        /*for (i <- 0 to 13) {
          gameboard.gb(i) = oldgb.gb(i)
        }
        for (i <- 0 to 13) {
          oldgb.gb(i) = vBoard.gb(i)
        }*/
        round -= 1
        undone = true
        notifyObservers
        print("undo \n")

      }
      //undoManager.undoStep
      notifyObservers
    } catch {
      case e: IllegalArgumentException => print(e)
    }

  }

  //TODO fix redo
  def redo(): Unit = {
    try {
      if(!undone) {
        throw new IllegalArgumentException("Es ist nur möglich einen Zug vorwärts zu machen")
      } else {
        for (i <- 0 to 13) {
          vBoard.gb(i) = gameboard.gb(i)
          gameboard.gb(i) = oldgb.gb(i)
          oldgb.gb(i) = vBoard.gb(i)
        }
        /*for (i <- 0 to 13) {
          gameboard.gb(i) = oldgb.gb(i)
        }
        for (i <- 0 to 13) {
          oldgb.gb(i) = vBoard.gb(i)
        }*/
        round += 1
        print("redo \n")
        undone = false
      }
      //undoManager.redoStep
      notifyObservers
    } catch {
      case e: IllegalArgumentException => print(e)
    }
  }

  //TODO fix restart
  def reset(): Unit = {
    gameboard = Gameboard(Array(0, amountStones, amountStones, amountStones, amountStones, amountStones, amountStones, 0, amountStones, amountStones, amountStones, amountStones, amountStones, amountStones))
    //gameboard.boardInit(6)
    round = 0
    notifyObservers
  }

  def checkWin(): Unit = {
    var x: Int = 0
    for (i <- 1 until 6 + 1) {
      //print("i: " + i)
      x += gameboard.gb(i)
    }
    var y: Int = 0
    for (i <- 1 until 6 + 1) {
      //print("i2: " + (i + 7))
      y += gameboard.gb(i + 7)
    }
    if (x == 0 || y == 0) win()
  }

  def win(): Unit = {
    var x: Int = 0
    for (i <- 1 until 6 + 1) {
      x += gameboard.gb(i)
    }
    var y: Int = 0
    for (i <- 1 until 6 + 1)
      y += gameboard.gb(i + p1)

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
  }

  def exit(): Unit = {
    sys.exit(0)
  }

  def save: Unit = {
    fileIO.save(this, board)
  }

  def load: Unit = {
    fileIO.load(this)
  }

}
