package actors

import actors.Producer.{Line, Produce}
import core.MultiThreadedActorContext

class ProducerTest extends MultiThreadedActorContext {

  val producer = system.actorOf(Producer.props(testActor))
  val targetString = "THE COMPLETE SHERLOCK HOLMES"
  val targetDrop = 4

  "Producer" must {
    "receive message Produce and send to testActor value of this line as string" in {
      producer ! Produce
      expectMsg(Line("", Some(producer)))
      producer ! Produce
      expectMsg(Line("", Some(producer)))
      producer ! Produce
      expectMsg(Line("", Some(producer)))
      producer ! Produce
      expectMsg(Line("", Some(producer)))
      producer ! Produce
      expectMsg(Line(targetString, Some(producer)))

    }
  }
}
