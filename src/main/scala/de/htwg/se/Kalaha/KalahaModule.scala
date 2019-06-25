package de.htwg.se.Kalaha

import com.google.inject.AbstractModule
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerImpl.Controller
import de.htwg.se.Kalaha.controller.controllerComponent.ControllerInterface
import net.codingwell.scalaguice.ScalaModule

class KalahaModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ControllerInterface].to[Controller]
    //TODO bind FileIO
  }

}