package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.{HtmlResponse, UrlMessage}

import scala.io.Source

class UrlActor extends Actor with ActorLogging {

  def getContent(url: String): String = {
    val fullUrl = s"http://$url"
    log.info("full url will be fetched: " + fullUrl)
    Source.fromURL(fullUrl).mkString
  }

  def receive = {
    case UrlMessage(url) =>
      log.info("url: " + url + " will be traversed")

      val content = getContent(url)

      sender() ! HtmlResponse(content, url)
  }
}

object UrlActor {
  val props = Props[UrlActor]
  case object Ack
}

