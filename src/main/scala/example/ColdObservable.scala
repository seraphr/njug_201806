package example

import io.reactivex.Observable

object ColdObservable extends App {
  val obs = Observable.create[String] { tEmitter =>
    println("start")
    tEmitter.onNext("hoge")
    tEmitter.onNext("fuga")
    tEmitter.onComplete()
    println("end")
  }

  obs.subscribe(s => println(s))
  println()
  obs.subscribe(s => println(s))
}

object ColdObservable2 extends App {
  val obs = Observable.fromArray(1,2,3).map(i => i * 2)
  obs.subscribe(i => println(i))
}

object ColdObservable3 extends App {
  def createObs(i: Int): Observable[Int] = Observable.fromArray(i, i + 1)

  val obs = Observable.fromArray(1,2,3).flatMap(i => createObs(i))
  obs.subscribe(i => println(i))
}

object ColdObservable4 extends App {
  val obs = Observable.fromArray(1,2,3,4,5,6,7,8).take(5)
  obs.subscribe(i => println(i))
}