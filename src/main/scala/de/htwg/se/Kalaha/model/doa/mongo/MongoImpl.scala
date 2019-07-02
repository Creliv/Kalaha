package de.htwg.se.Kalaha.model.doa.mongo

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.model.doa.DoaInterface
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import play.api.libs.json.Json

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Success

object MongoImpl extends DoaInterface {
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("kalahaDB")
  val collection: MongoCollection[Document] = database.getCollection("boardconfig")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def findById(id: Int): Future[(Int, Int, Int, String)] = {
    Future {
      var waitYOO = true
      var aStones, round = 0
      var board = ""
      val result: Observable[Document] = collection.find(equal("_id", id)).first()
      println(result)
      result.subscribe(new Observer[Document] {
        override def onNext(res: Document): Unit = {
          println("res: " + res)
          aStones = res("amountStones").asInt32.getValue
          round = res("round").asInt32().getValue
          board = res("boardvalues").asString().getValue
         }
        override def onError(e: Throwable): Unit = println("Failed")
        override def onComplete(): Unit = {
          waitYOO = false
          println("Completed")
        }
      })

      while (waitYOO) {
        Thread.sleep(10)
      }

      println((id, aStones, round, board))
      (id, aStones, round, board)
    }
  }

  def insert(id: Int, controller: Controller): Future[Int] = {
    Future{
      val document: Document = Document("_id" -> id, "amountStones" -> controller.amountStones,
        "round" -> controller.round, "boardvalues" -> controller.gameboard.gb.mkString(";").toString())
      val resFut = collection.insertOne(document).toFuture()
      Await.result(resFut, Duration.Inf)
      1
    }
  }

  def update(id: Int, aStones: Int, round: Int, boardArray: String): Future[Int] = {
    Future {
      collection.updateOne(equal("i", id), set("amountStones", aStones))
      collection.updateOne(equal("i", id), set("round", round))
      collection.updateOne(equal("i", id), set("boardvalues", boardArray))
      id
    }
  }

  def delete(id: Int): Future[Int] = {
    Future {
      collection.deleteOne(equal("i", id))
      id
    }
  }
}
