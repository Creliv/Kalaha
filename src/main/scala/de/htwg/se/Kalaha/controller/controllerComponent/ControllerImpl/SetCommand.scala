package de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl

import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard
import de.htwg.se.Kalaha.util.Command

class SetCommand (controller: Controller, inputX: Int, inputY: Int) extends Command {

  override def doStep: Unit = {
    controller.moveTui(inputX,inputY)
  }

  override def undoStep: Unit = {
    for (i <- 0 to 13) {
      controller.vBoard.gb(i) = controller.gameboard.gb(i)
      controller.gameboard.gb(i) = controller.oldgb.gb(i)
      controller.oldgb.gb(i) = controller.vBoard.gb(i)
    }
  }

  override def redoStep: Unit = {
    for (i <- 0 to 13) {
      controller.vBoard.gb(i) = controller.gameboard.gb(i)
      controller.gameboard.gb(i) = controller.oldgb.gb(i)
      controller.oldgb.gb(i) = controller.vBoard.gb(i)
    }
  }
}
