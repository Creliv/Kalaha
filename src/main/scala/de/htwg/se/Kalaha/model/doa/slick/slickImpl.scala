package de.htwg.se.Kalaha.model.doa.slick

import de.htwg.se.Kalaha.model.doa.DoaInterface
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}

object slickImpl extends DoaInterface{

  private val db = Database.forConfig("h2mem1")
  private val board = TableQuery[BoardTable]


  // TODO fetch values by ID
  def findById(id: Int): Future[(Int, Int, Int, String)] = {
    val q = db.run(board.filter(f => f.id === id).result.head)
//    val action = q.result
//    val result = db.run(action)
//    result.head
    q
  }

  // TODO insert amountStones, ronud, arrayValues as String
  def insert(id: Int, aStones: Int, round: Int, boardArray: String) = {
    //TODO
    Await.result(db.run(board.schema.createIfNotExists), Duration.Inf)
    db.run(board += (id, aStones, round, boardArray))
//    val insertActions = DBIO.seq(
//      board += (id, aStones, round, boardArray)
//    )
  }

  // TODO update column by ID
  def update(id: Int, aStones: Int, round: Int, boardArray: String) = {

  }

  // TODO delete column by ID
  def delete(id: Int) = {

  }
}
