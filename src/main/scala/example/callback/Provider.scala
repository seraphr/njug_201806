package example.callback

import java.io.Closeable
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

trait Provider[A] {
  def provide(f: Iterator[A] => Unit): Unit
}

trait Consumer[A] {
  def consume(itr: Iterator[A]): Unit
}

object Loan {
  def apply[R <: Closeable, A](r: R)(f: R => A): A = {
    try f(r) finally r.close()
  }
}

class FileLinesProvider(file: Path) extends Provider[String] {
  override def provide(f: Iterator[String] => Unit): Unit = {
    Loan(Files.newBufferedReader(file, StandardCharsets.UTF_8)) { in =>
      val itr = Iterator.continually(in.readLine).takeWhile(_ != null)
      f(itr)
    }
  }
}