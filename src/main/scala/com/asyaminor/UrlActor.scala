package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.UrlMessage
import com.asyaminor.UrlActor.Ack

class UrlActor extends Actor with ActorLogging {
  def receive = {
    case UrlMessage(url) =>
      println("got the message for :" + url)
      log.info("url: " + url + " will be traversed")
      sender() ! Ack
  }
}

object UrlActor {
  val props = Props[UrlActor]
  case object Ack
}

