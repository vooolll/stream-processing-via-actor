package actors

import actors.Consumer.Load
import core.MultiThreadedActorContext
import actors.ProcessingNode._
import akka.testkit.TestProbe

class ConsumerTest extends MultiThreadedActorContext {

  val parentActor = TestProbe()
  val fakeProducer = TestProbe()
  val consumer = parentActor.childActorOf(Consumer.props(testActor))

  "ConsumerTest" must {
    "send StartProducingQuestions to parent actor" in {
      consumer ! Load
      parentActor.expectMsg(StartProducingQuestions(None))
    }
  }
}
