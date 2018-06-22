package example.observable

import io.reactivex.Observable

object Runner {
  def runMap[A, B](p: Provider[A], f: A => B, c: Consumer[B]): Unit = {
    c.consume(p.provide().map(a => f(a)))
  }

  def runTake[A](p: Provider[A], i: Int, c: Consumer[A]): Unit = {
    c.consume(p.provide().take(i))
  }

  def runComplex(p: Provider[String],
                 pp: String => Provider[String],
                 f: String => Int,
                 c: Consumer[Int]): Unit = {

    val obs: Observable[Int] =
      p.provide()
        .flatMap(s => pp(s).provide())
        .map(s => f(s))
    c.consume(obs)
  }
}

object RunComplex extends App {
  def createProvider(aStr: String): Provider[String] =
    () => Observable.fromArray(aStr.tails.toArray: _*)

  val consumer: Consumer[Int] = {
    itr =>
      println("start consume")
      itr.subscribe(i => print(s"${i}, "))
      println
      println("end consume")
  }

  Runner.runComplex(
    createProvider("aaaa"),
    s => createProvider(s),
    s => s.length,
    consumer
  )
}