package pl.edu.agh.utils

import akka.actor.ActorRef
import pl.edu.agh.messages.{GetGroupedOut, GetOut}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.ask
import com.typesafe.config.ConfigFactory

object SinkUtils {
  lazy val awaitTime = ConfigFactory.load().getInt("results.awaitTime")

  def getResults[R](sink: ActorRef) = {
    import pl.edu.agh.utils.ActorUtils._
    import pl.edu.agh.utils.ActorUtils.Implicits._

    val dataF = akka.pattern.after(awaitTime milliseconds, using = system.scheduler)(sink ? GetOut)
    val data = Await.result(dataF, Duration.Inf).asInstanceOf[List[R]]

    data
  }
  def getGroupedResults[R](sink: ActorRef)(size: Int) = {
    import pl.edu.agh.utils.ActorUtils._
    import pl.edu.agh.utils.ActorUtils.Implicits._

    val dataF = akka.pattern.after(awaitTime milliseconds, using = system.scheduler)(sink ? GetGroupedOut(size))
    val data = Await.result(dataF, Duration.Inf).asInstanceOf[Iterator[List[R]]]

    data
  }
  def getResultsAsync[R](sink: ActorRef) = {
    import pl.edu.agh.utils.ActorUtils._
    import pl.edu.agh.utils.ActorUtils.Implicits._

    val dataF = akka.pattern.after(awaitTime milliseconds, using = system.scheduler)(sink ? GetOut)
    dataF.mapTo[List[R]]
  }
  def getGroupedResultsAsync[R](sink: ActorRef)(size: Int) = {
    import pl.edu.agh.utils.ActorUtils._
    import pl.edu.agh.utils.ActorUtils.Implicits._

    val dataF = akka.pattern.after(awaitTime milliseconds, using = system.scheduler)(sink ? GetGroupedOut(size))
    dataF.mapTo[Iterator[List[R]]]
  }
}
