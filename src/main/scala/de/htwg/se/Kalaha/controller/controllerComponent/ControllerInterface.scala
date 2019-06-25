package de.htwg.se.Kalaha.controller.controllerComponent

import de.htwg.se.Kalaha.view.gui.Gui

import scala.util._
import scala.concurrent._

trait ControllerInterface {

  def updateStones(x: Int): Unit

  def moveGui(inputX: Int, inputY: Int): Future[Unit]

  def moveTui(inputX: Int, inputY: Int): Future[Unit]

  def collectEnemyStones(last: Int): Unit

  def checkExtra(last: Int): Unit

  def undo(): Try[Unit]

  def redo(): Try[Unit]

  def reset(): Unit

  def checkWin(): Unit

  def win(): Unit

  def exit(): Unit

  def save(): Unit

  def load(): Unit

  def statusText(): String
}
