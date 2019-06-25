package de.htwg.se.Kalaha.view.tui

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.util.Observer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class Tui(controller: Controller) extends Observer {
  controller.addObserver(this)

  def startGame: Unit = {
    var input = ""
    welcomeMsg()
    print(showGameboard)
    print(printHelp())

    do {
      input = scala.io.StdIn.readLine()
      inputFct(input)

    } while (input != "exit")
  }

  //TODO parse moveinput
  def inputFct(input: String) = {
    checkPlayer
    input match {
      case "option" => askForAmountStonesStart
      case "help" => println(help)
      case "show" => println(showGameboard)
      case "undo" => controller.undo match {
        case Success(_) => println("Successfully undone step")
        case Failure(t) => println("Error: " + t)
      }
      case "redo" => controller.redo match {
        case Success(_) => println("Successfully redone step")
        case Failure(t) => println("Error: " + t)
      }
      case "reset" => controller.reset
      case "exit" => controller.exit
      case p if input.startsWith("p") && input.length <= 3 => {
        //println(startTurn(input.charAt(1).toString.toInt))
        print(startTurn(input.charAt(1).toString.toInt, input.charAt(2).toString.toInt))
      }
      case _ => println("Falsche Eingabe!")
    }
  }

  def askForAmountStonesStart = {
    print("\nfour oder 6 Steine? : ")
    var input = ""
    input = scala.io.StdIn.readLine()
    input match {
      case "4" => {
        controller.updateStones(4)
        controller.reset()
      }
      case "6" => {
        controller.updateStones(6)
        controller.reset()
      }
      case _ =>
        "\nEs ist nur moeglich mit 4 oder 6 Steinen zu starten."
    }
  }

  def checkPlayer: String = {
    val turn = controller.round % 2
    var str = ""
    if (turn == 0) {
      str += "\nSpieler " + Console.RED + "1 " + Console.RESET + "ist an der Reihe.\n"
    } else {
      str += "\nSpieler " + Console.BLUE + "2 " + Console.RESET + "ist an der Reihe.\n"
    }
    str
  }

def startTurn(inputX: Int, inputY: Int): String = {
  controller.moveTui(inputX, inputY).onComplete {
    case Success(v) => showGameboard()
    case Failure(e) => println("Error: " + e)
  }
  ""
}

  /*var turn = controller.round % 2
  if (turn == 0) {
    print("\nSpieler " + Console.RED + "1 " + Console.RESET + "ist an der Reihe.")
  } else {
    print("\nSpieler " + Console.BLUE + "2 " + Console.RESET + "ist an der Reihe.")
  }
  checkInputIFValid(input2) match {
    case false =>
      print("\nBitte richtige Werte angeben.")
    case true =>
      if (turn == 1) {
        input2 += 7
      }
      controller.move(input2)
  }
  print(showGameboard)*/


//def readUserInput(): Int = {
//  val a = scala.io.StdIn.readInt()
//  print("The value of a is " + a)
//  a
//}

// check for empty field MULDE
//def checkInputIFValid(index: Int): Boolean = index match {
//  case x if 1 to 6 contains x =>
//    var idx = index
//    if (controller.round % 2 == 1) {
//      idx += 7
//    }
//    if (controller.gameboard.gb(idx) > 0) {
//      true
//    } else {
//      false
//    }
//  case _ => false
//}

def welcomeMsg(): Unit = {
  var s = ""
  s += "\n"
  s += "Welcome to Kalaha!! :D\n" +
    "Spielregeln: ....."
  print(s)
  println(help)
}

def help: String = {
  var str = ""
  str += "je 6 (oder 4) Kugeln werden in die 12 kleinen Mulden gelegt \n\n Gewinner ist, wer bei Spielende die meisten Kugeln in seinem Kalaha hat.\n\n" +
     "Wer am Zuge ist, leert eine seiner Mulden und verteilt die Kugeln, jeweils eine, reihum im Gegenuhrzeigersinn in die nachfolgenden Mulden. " +
     "Dabei wird auch das eigene Kalaha gefüllt. Das Gegner Kalaha wird ausgelassen.\n\n" +
     "Fällt die letzte Kugel ins eigene Kalaha, ist der Spieler nochmals am Zuge.\n\n" +
     "Fällt die letzte Kugel in eine leere Mulde auf der eigenen Seite,  wird diese Kugel und alle Kugeln in der Gegner Mulde gegenüber, ins eigene Kalaha " +
     "gelegt und der Gegner hat den nächsten Zug.\n\n" +
     "Das Spiel ist beendet, wenn alle Mulden eines Spielers leer sind. Der Gegner bekommt dann alle Kugeln aus seinen Mulden in sein Kalaha.\n\n"
  str
}

def showGameboard(): String = {
  val gameboardString = new Array[String](14)
  for (i <- gameboardString.indices) {
    if (controller.gameboard.gb(i) < 10) {
      gameboardString(i) = " " + controller.gameboard.gb(i)
    }
    else {
      gameboardString(i) = " " + controller.gameboard.gb(i)
    }
  }
  var s = ""
  s += "\n"
  //TODO remove color or color without Console.XXX
  s +=
    Console.BLUE + "--------5----4----3----2----1----0-------\n" + Console.RESET +
      Console.BLUE + "|    | " + Console.RESET + gameboardString(13) + " | " + gameboardString(12) + " | " + gameboardString(11) + " | " + gameboardString(10) + " | " + gameboardString(9) + " | " + gameboardString(8) + Console.RED + " |    |\n" + Console.RESET +
      Console.BLUE + "| " + gameboardString(0) + " |" + Console.RESET + "-----------------------------" + Console.RED + "| " + gameboardString(7) + " |\n" + Console.RESET +
      Console.BLUE + "|    | " + Console.RESET + gameboardString(1) + " | " + gameboardString(2) + " | " + gameboardString(3) + " | " + gameboardString(4) + " | " + gameboardString(5) + " | " + gameboardString(6) + Console.RED + " |    |\n" + Console.RESET +
      Console.RED + "--------0----1----2----3----4----5-------\n" + Console.RESET
  s += "\n" + checkPlayer
  s
}

def checkWin(): Unit = {
  controller.checkWin()
  print(showGameboard())
  if (controller.p2win && controller.p1win) {
    print("Unentschieden!\n")
    //controller.exit()
  }
  if (controller.p1win) {
    print("Spieler 1 gewinnt mit " + controller.gameboard.gb(7) + "Punkten!\n")
    //controller.exit()
  } else if (controller.p2win) {
    print("Spieler 2 gewinnt mit " + controller.gameboard.gb(0) + "Punkten!\n")
    //controller.exit()
  }
}

def printHelp(): String = {
  var str = ""
  str +=
  "\nMögliche Eingaben:\n" +
  "     pXY => Zug des aktuellen Spielers starten (X: player1 = 0 / player2 = 1) (Y: Mulde (0-5)\n" +
  "     help => Spielregeln\n" +
  "     option => Feld mit four oder 6 Kugeln\n" +
  "     show => Anzeigen des Spielfelds\n" +
  "     undo => Letzten Zug rückgängig machen\n" +
  "     redo => Letzten Undo rückgängig machen\n" +
  "     reset => Spielfeld auf Anfang zurücksetzten\n" +
  "     exit => Beenden\n"
  str
}

override def update(): Unit = {
  print(checkPlayer)
  print(showGameboard)
  print(printHelp())
  //checkWin()
}

//ruft theoretisch nur funktionen auf
}
