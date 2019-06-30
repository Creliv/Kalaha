package de.htwg.se.Kalaha.model.doa.slick

import de.htwg.se.Kalaha.model.doa.DoaInterface
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}

import com.typesafe.config.ConfigFactory

object SlickImpl extends DoaInterface{

  private val conf = ConfigFactory.load
  private val db = Database.forConfig("h2mem1", conf)
  private val board = TableQuery[BoardTable]

  override def findById(id: Int): Future[(Int, Int, Int, String)] = {
    db.run(board.filter(f => f.id === id).result.head)
  }

  override def insert(id: Int, aStones: Int, round: Int, boardvalues: String) = {
    Await.result(db.run(board.schema.createIfNotExists), Duration.Inf)
    db.run(board += (id, aStones, round, boardvalues))
//    val insertActions = DBIO.seq(
//      board += (id, aStones, round, boardArray)
//    )
  }

  override def update(id: Int, aStones: Int, round: Int, boardArray: String) = {
    db.run(board.filter(f => f.id === id).update(id, aStones, round, boardArray))
  }

  override def delete(id: Int) = {
    db.run(board.filter(f => f.id === id).delete)
  }
}
