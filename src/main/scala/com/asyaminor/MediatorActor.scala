package com.asyaminor

import java.io.{FileWriter, BufferedWriter, File, PrintWriter}
import java.nio.charset.Charset
import java.nio.file.{Paths, Files}

import akka.actor.{Props, Actor, ActorLogging}
import com.asyaminor.MediatorActor._
import com.asyaminor.ParserActor.{PerformanceHtmlMessage, HtmlMessage}

/**
  * Created by ERKIN on 14/12/2015.
  */
class MediatorActor extends Actor with ActorLogging {

  //store a set for already visited urls
  var visited: Set[String] = Set()

  val urlActor = context.actorOf(UrlActor.props, "urlActor")
  val parseActor = context.actorOf(ParserActor.props, "parserActor")
  //TODO measure how long it takes to process a url
  //TODO build a tree of urls
  //TODO debug the code, trace it
  //TODO stop after a lot of visited url hits

  def dumpLinks() = {
    //TODO remove java.io.File api
    val pw = new PrintWriter(new File("links.txt"))

    visited foreach(link => pw.write(s"$link\n"))

    pw.close()
  }



  def storePerformanceDataToDisk(url: String, time: Long): Unit = {
    //TODO write the files to a tmp folder
    val perfDir = "/tmp/perf"
    val fileName = s"$perfDir/${getHost(url).get}.txt"

    if (!Files.exists(Paths.get(perfDir))) {
      Files.createDirectories(Paths.get(perfDir))
    }

    if (!Files.exists(Paths.get(fileName))) {
      Files.createFile(Paths.get(fileName))
    }

    val line = s"url: $url has a performance to http requests: $time ms"

    appendToFile(fileName, line)
  }

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

      parseActor ! HtmlMessage(html, url)
    case DumpLinksMsg(msg) =>
      log.info("will store the links to links.txt")
      dumpLinks()
      context.system.terminate()
    case PerformanceMsg(url) =>
      log.info(s"will measure performance of response times of $url")
      urlActor ! PerformanceMsg(url)
    case PerformanceResponse(body, url, time) =>
      log.info(s"got response for $url with the response time $time ms")
      storePerformanceDataToDisk(url, time)

      //parseActor ! PerformanceHtmlMessage(body, url)

    case ShutDownMsg(reason) =>
      context.system.terminate()
  }
}

object MediatorActor {
  val props = Props[MediatorActor]
  case class UrlMessage(url: String)
  case class HtmlResponse(html: String, url: String)
  case class ShutDownMsg(reason: String)
  case class DumpLinksMsg(msg: String)
  case class PerformanceMsg(url: String)
  case class PerformanceResponse(html: String, url: String, time: Long)

}
