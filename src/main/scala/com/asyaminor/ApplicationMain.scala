package com.asyaminor

import akka.actor.ActorSystem

import scala.io.StdIn

object ApplicationMain extends App {
    
  println("starting the url traverser")

  //ask for a url to visit
  handleIO()

  def callMediatorActor(url: String) = {

    //validate somehow

    //ask a url actor to traverse it
    //this may become a mediator in future

    //when you are finished, ask for another url

    val system = ActorSystem("UrlActorSystem")

    val urlActor = system.actorOf(UrlActor.props, "urlActor")
    urlActor ! UrlActor.UrlMessage("some unknown site")

    println("url actor got the msg")

    system.awaitTermination()
  }

  def handleIO(): Unit = {
    println("enter a url or 'q' to quit: ")

    val url = StdIn.readLine()

    url match {
      case "q" => println("quitting...")
      case _ => {
        println("will handle url: " + url)
        callMediatorActor(url)
        handleIO()
      }
    }
  }
}