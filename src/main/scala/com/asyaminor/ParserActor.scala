package com.asyaminor

import akka.actor.{Props, ActorLogging, Actor}
import com.asyaminor.ParserActor.HtmlMessage
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import org.jsoup.nodes.Element


class ParserActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case HtmlMessage(body) =>
      log.info(s"received body: $body")
      val browser = new Browser
      val doc = browser.parseString(body)

      val anchors: List[Element] = doc >> elementList("a")

      val links = anchors map (anc => anc.attr("href"))
      links foreach(link => log.info(s"raw link: $link"))
  }
}

object ParserActor {
  val props = Props[ParserActor]
  case class HtmlMessage(body: String)
}