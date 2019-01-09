package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging}

class TestingSystemSupervisor extends Actor with ActorLogging {

  override def receive: Receive = {
    case _ => unhandled()
  }

}
