package pl.edu.agh.main

import pl.edu.agh.actions.{Action, Action2}
import pl.edu.agh.dsl.WorkFlowDsl._
import pl.edu.agh.flows.{Out, In, Source}
import pl.edu.agh.utils.ActorUtils._
import pl.edu.agh.workflow.Workflow
import pl.edu.agh.workflow_patterns.synchronization._

object MultipleSyncMain extends App {

  val sum = Action2[Int, Int] { (in1, in2) =>
    in1 + in2
  }

  val mul = Action[List[Int], Int] { in =>
    in.reduceLeft[Int](_*_)
  }

  val sumProc = MultipleSync {
    sum
  }

  val mulProc = Sync {
    mul
  }

  val w = Workflow (
    name = "Sum of two inputs and multiply every three of them",
    numOfIns = 2,
    numOfOuts = 1,
    (ins: Seq[In[Int]], outs: Seq[Out[Int]]) => {
      (ins(0), ins(1)) ~>> sumProc
      sumProc.out ~>> outs(0)
    }
  )

  Source(1 to 6) ~> w.ins(0)
  Source(1 to 6) ~> w.ins(1)

  val res = w.run
  println(res)
  println(w)

}
