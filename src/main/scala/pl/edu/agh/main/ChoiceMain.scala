package pl.edu.agh.main

import pl.edu.agh.actions.Action
import pl.edu.agh.dsl.WorkFlowDsl._
import pl.edu.agh.flows.Source
import pl.edu.agh.workflow.Workflow
import pl.edu.agh.workflow_patterns.choice.Choice
import pl.edu.agh.utils.ActorUtils._

object ChoiceMain extends App {

  val action = Action[Int] { in =>
    in
  }

  val choiceProc = Choice (
    send (action),
    in => (in > 0, in == 0, in < 0)
  )

  val w = Workflow (
    "Example Choice Workflow",
    (ins, out) => {
      ins(0) ~>> choiceProc
      choiceProc.out1 ~>> out
      choiceProc.out3 ~>> out
    }
  )

  Source(-10 to 10) ~> w.ins(0)
  val res = w.run
  println(res)
  println(w)

}