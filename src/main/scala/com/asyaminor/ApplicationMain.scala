package com.asyaminor

import akka.actor.ActorSystem

import scala.io.StdIn

object ApplicationMain extends App {


  //TODO add nice --help section for the app
  //that shows the possible commands and parameters
  println("starting the url traverser")

  //ask for a url to visit
  handleIO()

  def callMediatorActor(url: String) = {

    //validate somehow
    val fullUrl = if (url.contains("http")) {
      url
    }
    else {
      "http" + url
    }

    //ask a url actor to traverse it
    //this may become a mediator in future

    //when you are finished, ask for another url

    val system = ActorSystem("UrlActorSystem")

    val mediator = system.actorOf(MediatorActor.props, "mediator")
    mediator ! MediatorActor.UrlMessage(fullUrl)

    println("mediator actor got the msg")
  }

  def shutDown(): Unit = {
    val system = ActorSystem("UrlActorSystem")

    system.shutdown()
  }

  def handleIO(): Unit = {
    println("enter a url or 'q' to quit: ")

    val url = StdIn.readLine()

    url match {
      case "q" =>
        println("quitting...")
        shutDown()
      case _ =>
        println("will handle url: " + url)
        callMediatorActor(url)
        handleIO()
    }
  }
}