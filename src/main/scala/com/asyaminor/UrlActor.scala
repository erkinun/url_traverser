package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.UrlActor.{Ack, UrlMessage}

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
  case class UrlMessage(url: String)
  case object Ack
}

