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

object MongoImpl{
  //TODO implement MongoDB
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("kalahaDB")
  val collection: MongoCollection[Document] = database.getCollection("boardconfig")

  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  def findById(id: Int)= {
    Future {
      val result = collection.find(equal("id", id)).first()
      

    }
  }

  def insert(id: Int, controller: Controller): Future[Int] = {
    Future{
      val document: Document = Document(Json.obj("_id" -> id, "amountStones" -> controller.amountStones,
        "round" -> controller.round, "boardvalues" -> controller.gameboard.gb.mkString(";")).toString())
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
