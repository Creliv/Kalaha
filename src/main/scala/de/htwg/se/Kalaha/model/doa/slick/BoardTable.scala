package de.htwg.se.Kalaha.model.doa.slick

import slick.jdbc.H2Profile.api._


class BoardTable(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "boardvalues"){
  //save gameboard array
  def id = column[Int]("ID", O.PrimaryKey)
  def stones = column[Int]("ASTONES")
  def round = column[Int]("ROUND")
  def boardvalues = column[String]("BOARDVALUES")


  def * = (id, stones, round, boardvalues)
}
