package app

import actors.Consumer.Load
import actors.ProcessingNode
import actors.ProcessingNode.GetConsumer
import akka.pattern.ask
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global


object ConsoleQuestionApp extends App {
  val system = ActorSystem("question-app")
  val ui = system.actorOf(Props[ConsoleClient])
  implicit val timeout = Timeout(100 second)
  val processingNode = system.actorOf(ProcessingNode.props(ui))
  val consumerFuture = processingNode.ask(GetConsumer()).mapTo[ActorRef]

  consumerFuture.map(ref => ref ! Load)

}

class ConsoleClient extends Actor {
  def receive = {
    case (x::xs) => println("Received data:")
      println((x::xs).mkString("\n"))
  }
}
