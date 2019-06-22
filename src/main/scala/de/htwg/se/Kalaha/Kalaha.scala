package de.htwg.se.Kalaha

import com.google.inject.Guice
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

object Kalaha {

        val injector = Guice.createInjector(new KalahaModule)
        val controller = injector.getInstance(classOf[ControllerInterface])

        def main(args: Array[String]): Unit = {
                controller.controllerInit().onComplete {
                        case Success(v) => println("game started " + v)
                        case Failure(e) => println(e)
                }


        }
}