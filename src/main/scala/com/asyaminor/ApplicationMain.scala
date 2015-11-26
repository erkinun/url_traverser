package com.asyaminor

import akka.actor.ActorSystem

object ApplicationMain extends App {
    
  println("starting the url traverser")
  println("ping actors will be url actors!")

  //ask for a url to visit

  //validate somehow

  //ask a url actor to traverse it
    //this may become a mediator in future

  //when you are finished, ask for another url
    
  val system = ActorSystem("UrlActorSystem")

  val urlActor = system.actorOf(UrlActor.props, "urlActor")
  urlActor ! UrlActor.UrlMessage("some unknown site")

  println("url actor got the msg")

  //val pingActor = system.actorOf(PingActor.props, "pingActor")
  //pingActor ! PingActor.Initialize
  system.awaitTermination()
}