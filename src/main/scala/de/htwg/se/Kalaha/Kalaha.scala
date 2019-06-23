package de.htwg.se.Kalaha

import com.google.inject.Guice
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.rest.WebServer
import de.htwg.se.Kalaha.util.Observable
import de.htwg.se.Kalaha.view.tui.Tui

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

object Kalaha extends Observable{

  val injector = Guice.createInjector(new KalahaModule)
  val controller = injector.getInstance(classOf[Controller])

  val thread1 = new Thread {
    override def run: Unit = {
      controller.controllerInit().onComplete {
        case Success(_) => notifyObservers
        case Failure(e) => println(e)
      }
    }
  }

  val thread2 = new Thread {
    override def run: Unit = {
      val wb = new WebServer(controller)
      wb.start
    }
  }
  thread1.start()
  thread2.start()
  //val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {
    //var input = ""

    /*do {
      input = scala.io.StdIn.readLine()
      tui.startGame(input)
    } while (input != "exit")
  */

  }
}