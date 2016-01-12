package com.asyaminor

import akka.actor.ActorSystem
import akka.actor.{Props, Actor, ActorLogging}
import com.asyaminor.MediatorActor.{ShutDownMsg, HtmlResponse, UrlMessage}
import com.asyaminor.ParserActor.HtmlMessage

/**
  * Created by ERKIN on 14/12/2015.
  */
class MediatorActor extends Actor with ActorLogging {

  //store a set for already visited urls
  var visited: Set[String] = Set()

  val system = ActorSystem("UrlActorSystem")
  val urlActor = system.actorOf(UrlActor.props, "urlActor")
  val parseActor = system.actorOf(ParserActor.props, "parserActor")
  //TODO measure how long it takes to process a url
  //TODO write a doc for fatjar with assembly

  //get the url message from main
  //send it to URL actors to get html
  //when you get HTML message
  //send it parser actor
  //when you get url finish message
  //print or persist the result
  override def receive: Receive = {

    case UrlMessage(url) =>
      visited(url) match {
        case true => log.info("url already visited")
        case false =>
          log.info(s"Mediator received url msg for: $url")
          visited = visited + url
          urlActor ! UrlMessage(url)
      }
    case HtmlResponse(html, url) =>
      val size = html.length
      log.info(s"$url is fetched with content length $size")
      log.debug(s"content is: $html")

      parseActor ! HtmlMessage(html)
    case ShutDownMsg(reason) =>
      context.system.terminate()
  }
}

object MediatorActor {
  val props = Props[MediatorActor]
  case class UrlMessage(url: String)
  case class HtmlResponse(html: String, url: String)
  case class ShutDownMsg(reason: String)
}
