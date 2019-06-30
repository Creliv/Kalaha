package de.htwg.se.Kalaha

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Guice
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.rest.WebServer
import de.htwg.se.Kalaha.util.{Observable, UiActor, startGui, startTui}
import de.htwg.se.Kalaha.view.tui.Tui

import scala.concurrent.Await

object Kalaha extends Observable{

  val injector = Guice.createInjector(new KalahaModule)
  val controller = injector.getInstance(classOf[Controller])

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  implicit val system = ActorSystem("actorSystem")
  val startUI = system.actorOf(Props[UiActor])

  startUI ! startGui(controller)

  val tuiFuture = startUI ? startTui(controller)
  val tui = Await.result(tuiFuture, timeout.duration).asInstanceOf[Tui]

  val thread = new Thread {
    override def run {
      val wb = new WebServer(tui)
      wb.start
    }
  }
  thread.start()


  def main(args: Array[String]): Unit = {
    var input = ""
    do {
        input = scala.io.StdIn.readLine()
        tui.inputFct(input)

      } while (input != "exit")

  }
}