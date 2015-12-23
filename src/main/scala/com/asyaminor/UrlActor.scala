package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.{HtmlResponse, UrlMessage}

import scalaj.http.Http

class UrlActor extends Actor with ActorLogging {

  def getContent(url: String): String = {
    val fullUrl = s"http://$url"
    log.info("full url will be fetched: " + fullUrl)
    Http(fullUrl).asString.body
  }

  def receive = {
    case UrlMessage(url) =>

      val content = getContent(url)

      sender() ! HtmlResponse(content, url)
  }
}

object UrlActor {
  val props = Props[UrlActor]
  case object Ack
}

