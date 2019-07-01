package de.htwg.se.Kalaha.model.doa

import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller

import scala.concurrent.Future

trait DoaInterface {

  def findById(id: Int): Future[(Int, Int, Int, String)]

  def insert(id: Int, controller: Controller): Future[Int]

  def update(id: Int, aStones: Int, round: Int, boardArray: String): Future[Int]

  def delete(id: Int): Future[Int]
}
