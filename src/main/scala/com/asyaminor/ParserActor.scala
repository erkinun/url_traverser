package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.{PerformanceMsg, UrlMessage}
import com.asyaminor.ParserActor.{PerformanceHtmlMessage, HtmlMessage}
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import org.jsoup.nodes.Element


class ParserActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case HtmlMessage(body, url) =>
      val links: List[String] = parseAnchors(body, url)
      links foreach(link => {
        log.info(s"raw link: $link")
        sender() ! UrlMessage(link)
      })
    case PerformanceHtmlMessage(body, url) =>
      val links: List[String] = parseAnchors(body, url)
      links foreach(link => {
        log.info(s"raw link: $link")
        sender() ! PerformanceMsg(link)
      })

  }

  private def parseAnchors(body: String, url: String): List[String] = {
    log.info(s"received body: $body")
    val browser = new Browser
    val doc = browser.parseString(body)
    doc.setBaseUri(url)

    val anchors: List[Element] = doc >> elementList("a")

    val links = anchors map (anc => anc.attr("abs:href"))
    links
  }
}

object ParserActor {
  val props = Props[ParserActor]
  case class HtmlMessage(body: String, url: String)
  case class PerformanceHtmlMessage(html: String, url: String)
}