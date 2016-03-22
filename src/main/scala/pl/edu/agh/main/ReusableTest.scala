package pl.edu.agh.main

import pl.edu.agh.actions.Action
import pl.edu.agh.flows.{Source, Out, In}
import pl.edu.agh.workflow.Workflow
import pl.edu.agh.workflow_patterns.merge.Merge
import pl.edu.agh.workflow_patterns.split.Split
import pl.edu.agh.utils.ActorUtils.Implicits._
import pl.edu.agh.dsl.WorkFlowDsl._

/** Prosty test sprawdzajacy, czy da się uzyc tego samego wezla wiecej niz jeden raz
  * oraz czy mozna zmienic akcje gdy uzyjemy wezla po raz drugi
  * */
object ReusableTest extends App {

  val multiplyByTen = Action[Int, Int] { in =>
    in * 10
  }

  val mergeProc = Merge[Int, Int] (
    name = "mergeProc",
    numOfOuts = 2,
    action = Action(identity),
    sendTo = "out1"
  )

  val splitProc = Split[Int, Int] (
    name = "splitProc",
    numOfOuts = 3,
    action = Action(identity)
  )

  val w = Workflow (
    "Reusable nodes test",
    numOfIns = 3,
    numOfOuts = 3,
    (ins: Seq[In[Int]], outs: Seq[Out[Int]]) => {
      ins(0) ~>> mergeProc
      ins(1) ~>> mergeProc
      ins(2) ~>> mergeProc

      splitProc changeActionOn multiplyByTen

      mergeProc.outs(1) ~> splitProc

      splitProc.outs(0) ~> mergeProc
      splitProc.outs(1) ~> mergeProc
      splitProc.outs(2) ~> mergeProc

      splitProc changeActionOn Action(identity)

      mergeProc.outs(1) ~> splitProc

      splitProc.outs(0) ~>> outs(0)
      splitProc.outs(1) ~>> outs(1)
      splitProc.outs(2) ~>> outs(2)
    }
  )

  Source(1 to 4) ~> w.ins(0)
  Source(5 to 8) ~> w.ins(1)
  Source(9 to 12) ~> w.ins(2)

  val res = w.run
  println(res)
  println(w)

}