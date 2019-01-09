package ru.shaldnikita.testing.system.backend.actors

import akka.actor.{Actor, ActorLogging, Props}

class TestingSystemSupervisor extends Actor with ActorLogging {

  val questionsStorage = context.actorOf(Props[QuestionsStorageActor])

  override def receive: Receive = {
    case _ => context.actorOf(TestActor.props(Vector("localhost:1234"),questionsStorage))
  }

}
