package de.htwg.se.Kalaha

import com.google.inject.Guice
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface
import de.htwg.se.Kalaha.rest.WebServer
import de.htwg.se.Kalaha.util.Observable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._

object Kalaha extends Observable{

        val injector = Guice.createInjector(new KalahaModule)
        val controller = injector.getInstance(classOf[ControllerInterface])

        def main(args: Array[String]): Unit = {

                val thread1 = new Thread {
                        override def run: Unit = {
                                controller.controllerInit().onComplete {
                                        case Success(_) => notifyObservers
                                        case Failure(e) => println(e)
                                }
                        }
                }

                val thread2 = new Thread {
                        override def run: Unit = {
                                val wb = new WebServer
                                wb.start
                        }
                }
                thread1.start()
                thread2.start()
        }
}