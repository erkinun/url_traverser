package com.asyaminor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
 * Created by eunlu on 26/11/2015.
 */
class UrlActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A Url actor" must {
    "send back a ack on a UrlMessage" in {
      val urlActor = system.actorOf(UrlActor.props)
      urlActor ! UrlActor.UrlMessage("www.google.com")
      expectMsg(UrlActor.Ack)
    }
  }
}
