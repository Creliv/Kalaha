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
    Some(setBoardPieces)
  }


  override def redoMove: Option[Unit] = {
    Some(setBoardPieces)
  }

  def setBoardPieces: Unit = {
    for (i <- 0 to 13) {
      controller.vBoard.gb(i) = controller.gameboard.gb(i)
      controller.gameboard.gb(i) = controller.oldgb.gb(i)
      controller.oldgb.gb(i) = controller.vBoard.gb(i)
    }
  }
}
