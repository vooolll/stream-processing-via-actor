package actors

import actors.ProcessingNode._
import actors.Producer.{EndOfFileStream, Line, Produce}
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.pipe

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ProcessingNode {

  case class ShouldProduce(out: ActorRef)
  case class StartProducingQuestions(producer: Option[ActorRef])
  case object StopProducingQuestions
  case class GetConsumer()

  def props(uiActor: ActorRef) = Props(new ProcessingNode(uiActor))
  def isQuestion(trimmed: String) = trimmed.endsWith("?") || trimmed.endsWith("?\"")
}

class ProcessingNode(uiActor: ActorRef) extends Actor {

  var shouldProduce = false

  val consumer = context.actorOf(Consumer.props(uiActor))
  val producer = context.actorOf(Producer.props(self))

  def receive = {
    case StartProducingQuestions(testProducer) =>
      shouldProduce = true
      testProducer.getOrElse(producer) ! Produce
      producer ! Produce
    case StopProducingQuestions =>
      shouldProduce = false
    case Line(text, testProducer) =>
      val trimmed = text.trim
      if (isQuestion(trimmed)) {
        consumer ! trimmed
      }
      if (shouldProduce) {
        testProducer.getOrElse(producer) ! Produce
      }

    case GetConsumer() =>
      Future.successful(consumer) pipeTo sender()

    case ShouldProduce(out) => out ! shouldProduce
    case EndOfFileStream =>
      consumer ! EndOfFileStream
  }

}
