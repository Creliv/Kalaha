package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.util.Command

class SetCommand (controller: Controller, index: Int) extends Command {

  override def doStep: Unit = {
    controller.move(index)
  }

  //TODO fix SetCommand Undo
  override def undoStep: Unit = {

  }

  //TODO fix SetCommand Redo
  override def redoStep: Unit = {

  }
}
