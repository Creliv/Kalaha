package de.htwg.se.Kalaha.util

import scala.collection.mutable.ListBuffer

trait Observer {
  def update()
}
/*class Observable {
  var subscribers: Vector[Observer] = Vector()
  def addObserver(s: Observer): Unit = subscribers = subscribers :+ s
  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers: Unit = subscribers.foreach(o => o.update)
}*/

class Observable {
  var observers: ListBuffer[Observer] = ListBuffer.empty

  def addObserver(o: Observer): Unit = {
    observers.+=(o)
  }

  def removeObserver(o: Observer): Unit = {
    observers.-=(o)
  }

  def notifyObservers(): Unit = {
    for (observer <- observers) {
      observer.update()
    }
  }
}

