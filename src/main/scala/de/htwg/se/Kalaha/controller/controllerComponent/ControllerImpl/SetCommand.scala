package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.util.Command

class SetCommand (controller: Controller, index: Int) extends Command {

  override def doStep: Unit = {
    controller.move(index)
  }

  //TODO fix SetCommand Undo
  override def undoStep: Unit = {
    for (i <- 0 to 13) {
      controller.vBoard.gb(i) = controller.gameboard.gb(i)
      controller.gameboard.gb(i) = controller.oldgb.gb(i)
      controller.oldgb.gb(i) = controller.vBoard.gb(i)
    }
  }

  //TODO fix SetCommand Redo
  override def redoStep: Unit = {
    for (i <- 0 to 13) {
      controller.vBoard.gb(i) = controller.gameboard.gb(i)
      controller.gameboard.gb(i) = controller.oldgb.gb(i)
      controller.oldgb.gb(i) = controller.vBoard.gb(i)
    }
  }
}
