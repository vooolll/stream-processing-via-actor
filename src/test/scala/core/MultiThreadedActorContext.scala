package core

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

class MultiThreadedActorContext extends TestKit(ActorSystem("gift-repeat-test"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

}
