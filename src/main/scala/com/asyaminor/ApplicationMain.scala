package com.asyaminor

import java.util.{TimerTask, Timer}
import akka.actor.ActorSystem
import com.asyaminor.MediatorActor.ComparisonMsg
import com.asyaminor.remote.RemoteUrlActor


import scala.io.StdIn
import io.Source
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object ApplicationMain extends App {

  //TODO handle the alive message somehow

  val system = ActorSystem("UrlActorSystem")
  val mediator = system.actorOf(MediatorActor.props, "mediator")
  val remote = system.actorOf(RemoteUrlActor.props, "remote")

  //TODO add nice --help section for the app
  //that shows the possible commands and parameters
  println("starting the url traverser")

  //ask for a url to visit
  handleIO()

  def callMediatorActor(url: String) = {

    val fullUrl: String = validateUrl(url)

    //ask a url actor to traverse it
    //this may become a mediator in future

    //when you are finished, ask for another url

    mediator ! MediatorActor.UrlMessage(fullUrl)

    println("mediator actor got the msg")
  }

  def shutDown(): Unit = {
    mediator ! MediatorActor.ShutDownMsg("user quitted")
  }

  def dumpLinks(): Unit = {
    mediator ! MediatorActor.DumpLinksMsg("user wants dump!")
  }

  def measurePerf(host: String): Unit = {
    mediator ! MediatorActor.PerformanceMsg(validateUrl(host))
  }

  def comparePerfFile(filename: String): Unit = {

    val fileItself = Source.fromFile(filename)

    val compareFuture = Future {
      val t = new Timer()
      val task = new TimerTask {
        override def run(): Unit = {
          println("comparing")
          Source.fromFile(filename) getLines() foreach{ avgTuple =>
            val (host, avg) = {
              val words = avgTuple.split(" ")
              (words(0), words(1))
            }
            println("sending comp msg")
            mediator ! ComparisonMsg(host, avg.toFloat.toLong)
          }
        }
      }

      t.schedule(task, 0L, 1000L * 30)
    }


    Await.result(compareFuture, 1 hour)
  }

  def measurePerfFile(filename: String): Unit = {
    Source.fromFile(filename).getLines() foreach(host =>
      mediator ! MediatorActor.PerformanceMsg(validateUrl(host)))
  }

  def profileForFile(filename: String): Unit = {
    // will work for one hour
    // will work every one minute

    val profileFuture = Future {
      val t = new Timer()
      val task = new TimerTask {
        override def run(): Unit = {
          println(s"profiling using file: $filename")
          measurePerfFile(filename)
        }
      }

      t.schedule(task, 0L, 1000L * 60)
    }

    Await.result(profileFuture, 1 hour)

  }

  def handleIO(): Unit = {
    println("enter a url or 'q' to quit: ")

    val url = StdIn.readLine()

    url match {
      case "-h" =>
        helpText
        handleIO()
      case "-test" =>
        println("testing the remote actor")
        remote ! RemoteUrlActor.ALIVE
        handleIO()
      case "q" =>
        println("quitting...")
        shutDown()
      case "qd" =>
        println("dumping the links")
        dumpLinks()
        //TODO 1 - add a cron style running for a period of time, say for 2 hours every 1 minute
        //TODO 3 - maybe use some parser combinators? //take a look at http://stackoverflow.com/a/3183991/219586
      case urlx if urlx.startsWith("--measure") =>
        val host = urlx.replace("--measure", "").trim
        println(s"we are going to measure: $host")
        measurePerf(host)
        handleIO()
      case urlx if urlx.startsWith("--file") =>
        val filename = urlx.replace("--file", "").trim
        measurePerfFile(filename)
        handleIO()
      case urlx if urlx.startsWith("--profile") =>
        val filename = urlx.replace("--profile", "").trim
        profileForFile(filename)
      case command if command.startsWith("--compare") =>
        val filename = command.replace("--compare", "").trim
        comparePerfFile(filename)
      case "" =>
        println("empty line!!")
        handleIO()
      case _ =>
        println("will handle url: " + url)
        callMediatorActor(url)
        handleIO()
    }
  }

  def helpText: Unit = {
    println("q for quit")
    println("-h for this help screen")
    println("-test to test remote actor")
    println("qd to dump the links")
    println("--measure site , to measure performance of a site")
    println("--file filename, to measure a list of sites from a file")
    println("--profile filename, to profile a collection of hosts for one hour")
    println("--compare filename, to compare to a pre calculated average response times")
    println("any other string to traverse the links on it")
  }
}