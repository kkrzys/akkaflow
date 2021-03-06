package pl.edu.agh.workflow.elements

import akka.actor.{Actor, ActorContext, ActorLogging, Props}
import pl.edu.agh.messages.{Get, GetGroupedOut, GetOut, ResultMessage}

class Sink[R] extends Actor with ActorLogging {

  var out = List.empty[R]

  def receive = {
    case ResultMessage(data: R) =>
      //log.info("CHILD")
      out :+= data
    case GetOut =>
      val o = out
      //This line is needed to feed workflow feature
      //out = List.empty[R]
      sender ! o
    case GetGroupedOut(size: Int) =>
      val o = out
      //This line is needed to feed workflow feature
      //out = List.empty[R]
      sender ! o.grouped(size)
    case Get =>
      sender ! this
  }

}

object Sink {
  def apply[R](context: ActorContext) = context.actorOf(Sink.props)
  def apply[R](name: String, context: ActorContext) = context.actorOf(Sink.props, name)
  def props[R] = Props[Sink[R]]
}
