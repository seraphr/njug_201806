package example.callback

object Runner {
  def runMap[A, B](p: Provider[A], f: A => B, c: Consumer[B]): Unit =
    p.provide { itr =>
      c.consume(itr.map(f))
    }

  def runTake[A](p: Provider[A], i: Int, c: Consumer[A]): Unit =
    p.provide { itr =>
      c.consume(itr.take(i))
    }

  // providerから出てきた文字列をもとに新しいproviderを取得して、
  // それを変換した上でConsumerにわたす
  // Consumerに複数回Iteratorを渡すことになるので、
  // 全体の１回を認識するために、Consumer側もCallbackにする
  def runComplex(p: Provider[String],
                 pp: String => Provider[String],
                 f: String => Int,
                 cp: (Consumer[Int] => Unit) => Unit): Unit = {
    cp { consumer =>
      p.provide { itr =>
        itr.foreach { str =>
          pp(str).provide { itr2 =>
            consumer.consume(itr2.map(f))
          }
        }
      }
    }
  }
}

object RunComplex extends App {
  def createProvider(aStr: String): Provider[String] =
    f => f(aStr.tails)

  val consumer: Consumer[Int] =
    itr => itr.foreach(i => print(s"${i}, "))

  Runner.runComplex(
    createProvider("aaaa"),
    s => createProvider(s),
    s => s.length,
    f => {
      println("start consume")
      f(consumer)
      println; println("end consume")
    }
  )
}