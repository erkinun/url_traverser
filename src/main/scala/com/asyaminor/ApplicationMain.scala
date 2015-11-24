package com.asyaminor

import akka.actor.ActorSystem

object ApplicationMain extends App {
    
  println("starting the url traverser")
  println("ping actors will be url actors!")
    
  val system = ActorSystem("UrlActorSystem")
  val pingActor = system.actorOf(PingActor.props, "pingActor")
  pingActor ! PingActor.Initialize
  // This example app will ping pong 3 times and thereafter terminate the ActorSystem - 
  // see counter logic in PingActor
  system.awaitTermination()
}