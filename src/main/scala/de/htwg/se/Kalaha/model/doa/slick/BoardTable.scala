package de.htwg.se.Kalaha.model.doa.slick

import slick.jdbc.H2Profile.api._

case class BoardConfig(id: Int, )

class BoardTable(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "boardvalues"){
  //save gameboard array
  def id = column[Int]("ID", O.PrimaryKey)
  def stones = column[Int]("ASTONES")
  def round = column[Int]("ROUND")
  def board = column[String]("BOARD")


  def * = (id, stones, round, board)
}
