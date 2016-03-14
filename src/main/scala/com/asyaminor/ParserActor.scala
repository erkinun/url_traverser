package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.MediatorActor.UrlMessage
import com.asyaminor.ParserActor.HtmlMessage
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import org.jsoup.nodes.Element


class ParserActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case HtmlMessage(body, url) =>
      log.info(s"received body: $body")
      val browser = new Browser
      val doc = browser.parseString(body)
      doc.setBaseUri(url)

      val anchors: List[Element] = doc >> elementList("a")

      val links = anchors map (anc => anc.attr("abs:href"))
      links foreach(link => {
        log.info(s"raw link: $link")
        sender() ! UrlMessage(link)
      })

  }
}

object ParserActor {
  val props = Props[ParserActor]
  case class HtmlMessage(body: String, url: String)
}