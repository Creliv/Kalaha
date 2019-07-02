package de.htwg.se.Kalaha.model.doa.slick

import de.htwg.se.Kalaha.model.doa.DoaInterface
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import com.typesafe.config.ConfigFactory
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller

object SlickImpl extends DoaInterface{

  private val conf = ConfigFactory.load
  private val db = Database.forConfig("h2mem1", conf)
  private val board = TableQuery[BoardTable]

  override def findById(id: Int): Future[(Int, Int, Int, String)] = {
    db.run(board.filter(f => f.id === id).result.head)
  }

  override def insert(id: Int, controller: Controller): Future[Int] = {
    Await.result(db.run(board.schema.createIfNotExists), Duration.Inf)
    db.run(board += (id, controller.amountStones, controller.round, controller.gameboard.gb.mkString(";")))
  }

  override def update(id: Int, aStones: Int, round: Int, boardArray: String): Future[Int] = {
    db.run(board.filter(f => f.id === id).update(id, aStones, round, boardArray))
  }

  override def delete(id: Int): Future[Int] = {
    db.run(board.filter(f => f.id === id).delete)
  }
}
