package example.observable

import java.io.Closeable
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import io.reactivex.Observable

trait Provider[A] {
  def provide(): Observable[A]
}

trait Consumer[A] {
  def consume(itr: Observable[A]): Unit
}

object Loan {
  def apply[R <: Closeable, A](r: R)(f: R => A): A = {
    try f(r) finally r.close()
  }
}

class FileLinesProvider(file: Path) extends Provider[String] {
  override def provide(): Observable[String] = Observable.create[String] {
    // 本来必要なエラー処理は飛ばしている
    tEmitter => {
      Loan(Files.newBufferedReader(file, StandardCharsets.UTF_8)) { in =>
        val itr = Iterator.continually(in.readLine).takeWhile(_ != null)
        itr.foreach { s => tEmitter.onNext(s) }
        tEmitter.onComplete()
      }
    }
  }
//  override def provide(): Observable[String] = Observable.create[String] {
//    tEmitter =>
//      try {
//        Loan(Files.newBufferedReader(file, StandardCharsets.UTF_8)) { in =>
//          val itr = Iterator.continually(in.readLine).takeWhile(_ != null)
//          itr.foreach { s => tEmitter.onNext(s) }
//          tEmitter.onComplete()
//        }
//      } catch {
//        case e: Throwable => tEmitter.onError(e)
//      }
//  }
}