package de.htwg.se.Kalaha.model.fileIoComponent

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.gameboardController.GameboardImpl.Gameboard

import scala.util.Try

trait FileIOInterface {

  def load(controller: Controller, file: String): Try[Unit]
  def save(controller: Controller, file: String): Try[Unit]

}
