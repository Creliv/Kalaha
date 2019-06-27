package de.htwg.se.Kalaha.util

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller

/*
class UndoManager {
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil
  def doStep(command: Command) : Unit = {
    undoStack = command :: undoStack
    command.doStep
  }
  def undoStep :Unit = {
    undoStack match {
      case Nil =>
      case head :: stack => {
        head.undoStep
        undoStack = stack
        redoStack = head :: redoStack
      }
    }
  }
  def redoStep : Unit = {
    redoStack match {
      case Nil =>
      case head :: stack => {
        head.redoStep
        redoStack = stack
        undoStack = head :: undoStack
      }
    }
  }
}*/

trait UndoManager {

  def undoMove: Option[Unit]

  def redoMove: Option[Unit]
}

class UndoManagerImpl(controller: Controller) extends UndoManager {

  override def undoMove: Option[Unit] = {
    Some(controller.gameboard.setBoardPieces(controller.oldgb, controller.vBoard))
  }

  override def redoMove: Option[Unit] = {
    Some(controller.gameboard.setBoardPieces(controller.oldgb, controller.vBoard))
  }
}
