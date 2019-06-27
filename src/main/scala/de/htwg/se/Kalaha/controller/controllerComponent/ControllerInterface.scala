package de.htwg.se.Kalaha.controller.controllerComponent

import de.htwg.se.Kalaha.view.gui.Gui

import scala.util._
import scala.concurrent._

trait ControllerInterface {

  def updateStones(x: Int): Unit

  def moveGui(inputX: Int, inputY: Int): Future[Unit]

  def moveTui(inputX: Int, inputY: Int): Future[Unit]

  def undo(): Try[Unit]

  def redo(): Try[Unit]

  def reset(): Unit

  def exit(): Unit

  def save(file: String): Unit

  def load(file: String): Unit

  def statusText(): String
}
