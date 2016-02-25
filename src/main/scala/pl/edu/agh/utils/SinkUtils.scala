package pl.edu.agh.utils

import akka.actor.ActorRef
import pl.edu.agh.messages.GetOut

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask

object SinkUtils {
  def getResults[K](sink: ActorRef) = {
    import pl.edu.agh.utils.ActorUtils.{system, timeout}

    val dataF = akka.pattern.after(200 milliseconds, using = system.scheduler)(sink ? GetOut)
    val data = Await.result(dataF, Duration.Inf).asInstanceOf[List[K]]

    data
  }
}