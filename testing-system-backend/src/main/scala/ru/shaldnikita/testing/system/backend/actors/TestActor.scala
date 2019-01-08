package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}
import ru.shaldnikita.testing.system.backend.messages.test.StartTest

class TestActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case _: StartTest => {
      context.actorOf(Props[DatabaseWriter])
    }
  }
}
