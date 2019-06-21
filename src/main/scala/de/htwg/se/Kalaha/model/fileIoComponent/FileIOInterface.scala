package de.htwg.se.Kalaha.model.fileIoComponent

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

trait FileIOInterface {

  def load(controller: Controller)
  def save(controller: Controller, board:Gameboard):Unit

}
