package actors

import actors.ProcessingNode._
import actors.Producer.Produce
import akka.testkit.TestProbe
import core.MultiThreadedActorContext

class ProcessingNodeTest extends MultiThreadedActorContext {


  val testProducer = TestProbe()
  def fixture = new {
    val processingNode = system.actorOf(ProcessingNode.props(TestProbe().ref))
  }

  "ProcessingNode" must {

    "not produce anything by default" in {
      val processingNode = fixture.processingNode
      processingNode ! ShouldProduce(testActor)
      expectMsg(false)
    }

    "trigger Producer to produce lines when it receives StartProducingQuestions" in {
      val processingNode = fixture.processingNode
      processingNode ! StartProducingQuestions(Some(testProducer.ref))
      processingNode ! ShouldProduce(testActor)
      testProducer.expectMsg(Produce)
      expectMsg(true)
    }

    "trigger Producer to stop producing line when it receives StopProducingQuestion" in {
      val processingNode = fixture.processingNode
      processingNode ! StartProducingQuestions(Some(testProducer.ref))
      testProducer.expectMsg(Produce)

      processingNode ! StopProducingQuestions
      processingNode ! ShouldProduce(testActor)
      expectMsg(false)
    }
  }
}
