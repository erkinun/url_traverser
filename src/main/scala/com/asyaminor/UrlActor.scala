package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.{PerformanceResponse, PerformanceMsg, HtmlResponse, UrlMessage}

import scalaj.http.Http

class UrlActor extends Actor with ActorLogging {

  type MeasureResult = (String, Long)

  def getContent(url: String): String = {
    val fullUrl = url
    log.info("full url will be fetched: " + fullUrl)
    Http(fullUrl).asString.body
  }

  def measureHost(url: String): MeasureResult = {
    //first just get the response and calculate
    //next do 50 times and average
    //next take a look at warmup algorithms

    val result = time{
      Http(url).asString.body
    }

    (result._1, result._2)
  }

  def receive = {
    case UrlMessage(url) =>

      val content = getContent(url)

      sender() ! HtmlResponse(content, url)

    case PerformanceMsg(url) =>

      val (body, time) = measureHost(url)

      sender() ! PerformanceResponse(body, url, time)
  }
}

object UrlActor {
  val props = Props[UrlActor]
  case object Ack
}

