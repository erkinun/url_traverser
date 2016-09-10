package com.asyaminor.remote

import akka.actor.{Props, ActorSystem, Actor}
import com.asyaminor.MediatorActor
import com.asyaminor.MediatorActor.UrlMessage
import com.asyaminor._

/**
  * Created by eunlu on 10/09/2016.
  */
class RemoteUrlActor extends Actor {

  //val system = ActorSystem("UrlActorSystem")
  val system = ApplicationMain.system
  val mediator = system.actorOf(MediatorActor.props)

  val ALIVE_MSG = "Alive"

  //TODO also send from MAIN an alive message
  def receive: Receive = {
    case msg: String => {
      println(s"received the string: $msg")
      if (msg.equals(ALIVE_MSG)){
        sender ! "Remote Url Alive"
      }
      else {
        mediator ! UrlMessage(validateUrl(msg))
      }

    }
  }
}

object RemoteUrlActor {
  val props = Props[RemoteUrlActor]
  val ALIVE = "Alive"
}
