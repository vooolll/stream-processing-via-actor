package actors

import actors.Consumer.Load
import actors.ProcessingNode.{StartProducingQuestions, StopProducingQuestions}
import actors.Producer.EndOfFileStream
import akka.actor.{Actor, ActorRef, Props}

object Consumer {
  def props(uiActor: ActorRef) = Props(new Consumer(uiActor))
  case object Load
}

class Consumer(uiActor: ActorRef) extends Actor {

  val processingNode = context.parent

  var questions = List.empty[String]
  val maxBoundedQuestions = 10

  def receive = {
    case Load =>
      println("Loading questions")
      processingNode ! StartProducingQuestions(None)
    case s: String => questions = s::questions
      if (questions.length < maxBoundedQuestions) {

      } else {
        uiActor ! questions
        questions = List.empty[String]
        processingNode ! StopProducingQuestions(None)
        Thread.sleep(2000)
        println("Loading new batch")
        processingNode ! StartProducingQuestions(None)
      }
    case EndOfFileStream =>
      println("FINISH: No data anymore")
      processingNode ! StopProducingQuestions(None)
  }

}
