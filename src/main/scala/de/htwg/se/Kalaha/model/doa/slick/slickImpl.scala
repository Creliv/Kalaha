package de.htwg.se.Kalaha.model.doa.slick

import de.htwg.se.Kalaha.model.doa.DoaInterface
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}

import com.typesafe.config.ConfigFactory

object slickImpl extends DoaInterface{

  private val conf = ConfigFactory.load
  private val db = Database.forConfig("h2mem1", conf)
  private val board = TableQuery[BoardTable]

  def findById(id: Int): Future[(Int, Int, Int, String)] = {
    val q = db.run(board.filter(f => f.id === id).result.head)
    q
  }

  def insert(id: Int, aStones: Int, round: Int, boardvalues: String) = {
    Await.result(db.run(board.schema.createIfNotExists), Duration.Inf)
    db.run(board += (id, aStones, round, boardvalues))
//    val insertActions = DBIO.seq(
//      board += (id, aStones, round, boardArray)
//    )
    println("yooo")
  }

  def update(id: Int, aStones: Int, round: Int, boardArray: String) = {
    db.run(board.filter(f => f.id === id).update(id, aStones, round, boardArray))
  }

  def delete(id: Int) = {
    db.run(board.filter(f => f.id === id).delete)
  }
}
