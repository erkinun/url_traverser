package com.asyaminor

import akka.actor.ActorSystem
import akka.actor.{Props, Actor, ActorLogging}
import com.asyaminor.MediatorActor.UrlMessage

/**
  * Created by ERKIN on 14/12/2015.
  */
class MediatorActor extends Actor with ActorLogging {

  val system = ActorSystem("UrlActorSystem")
  val urlActor = system.actorOf(UrlActor.props, "urlActor")

  //TODO measure how long it takes to process a url

  //get the url message from main
  //send it to URL actors to get html
  //when you get HTML message
  //send it parser actor
  //when you get url finish message
  //print or persist the result
  override def receive: Receive = {
    case UrlMessage(url) => {
      log.info(s"Mediator received url msg for: $url")
      urlActor ! UrlMessage(url)
    }
  }
}

object MediatorActor {
  val props = Props[MediatorActor]
  case class UrlMessage(url: String)
}
