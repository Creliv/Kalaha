package de.htwg.se.Kalaha.util

import de.htwg.se.Kalaha.view.tui.Tui
import de.htwg.se.Kalaha.view.gui.Gui
import akka.actor.Actor
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class UiActor extends Actor {

  def receive: Receive = {
    case startGui(controller) => new Gui(controller)
    case startTui(controller) => {
      val tui = Tui(controller)
      sender() ! tui
    }
  }
}

//Actor message
case class startGui(controller: Controller)
case class startTui(controller: Controller)
