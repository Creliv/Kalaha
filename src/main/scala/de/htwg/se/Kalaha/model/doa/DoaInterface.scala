package de.htwg.se.Kalaha.model.doa

import scala.concurrent.Future

trait DoaInterface {

  def findById(id: Int): Future[(Int, Int, Int, String)]

  def insert(id: Int, aStones: Int, round: Int, boardvalues: String)

  def update(id: Int, aStones: Int, round: Int, boardArray: String)

  def delete(id: Int)
}
