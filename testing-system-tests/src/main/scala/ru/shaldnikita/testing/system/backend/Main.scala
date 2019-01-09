import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

object Main extends App {

  override def main(args: Array[String]): Unit = {
    implicit val ac: ActorSystem = ActorSystem("testing-system")
    implicit val ec: ExecutionContext = ac.dispatcher
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()



  }

}