package de.htwg.se.Kalaha.controller.controllerComponent

object GameStatus extends Enumeration {
  type GameStatus = Value
  val IDLE, NEW, UNDO, REDO, WON = Value

  val map = Map[GameStatus, String](
    IDLE -> "waiting for input",
    NEW -> "New game started",
    UNDO -> "Undone one step",
    REDO -> "Redone one step",
    WON -> "Game over"
  )

  def message(gameStatus: GameStatus) = {
    map(gameStatus)
  }
}
