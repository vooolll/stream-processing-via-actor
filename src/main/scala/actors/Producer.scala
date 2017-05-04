package actors

import actors.Producer.{EndOfFileStream, Line, Produce}
import akka.actor.{Actor, ActorRef, Props}

import scala.io.Source

object Producer {
  case object Produce
  case class Line(text: String, producer: Option[ActorRef])
  case object EndOfFileStream

  def props(processingNode: ActorRef) = Props(new Producer(processingNode))
}

class Producer(processingNode: ActorRef) extends Actor {

  val lineStream = Source.fromFile("src/main/resources/cano.txt").getLines

  def receive = {
    case Produce =>
      val iterator = lineStream
      if (iterator.hasNext) {
        val text = iterator.next()
        processingNode ! Line(text.trim, Some(self))
      } else {
        processingNode ! EndOfFileStream
      }
  }
}
