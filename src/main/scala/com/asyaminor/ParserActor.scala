package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.ParserActor.HtmlMessage

class ParserActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case HtmlMessage(body) =>
      log.info(s"received body: $body")
  }
}

object ParserActor {
  val props = Props[ParserActor]
  case class HtmlMessage(body: String)
}