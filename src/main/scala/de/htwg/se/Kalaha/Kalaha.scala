package de.htwg.se.Kalaha

import com.google.inject.Guice
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface

object Kalaha {

        val injector = Guice.createInjector(new KalahaModule)
        var controller = injector.getInstance(classOf[ControllerInterface])

        def main(args: Array[String]): Unit = {
                controller.controllerInit().isCompleted
        }
}