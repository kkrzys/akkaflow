package pl.edu.agh.workflow

import pl.edu.agh.workflow.elements.Out

trait Runnable[R] {
  def run: Seq[Out[R]]
  def start: Unit
  def stop: Unit
}
