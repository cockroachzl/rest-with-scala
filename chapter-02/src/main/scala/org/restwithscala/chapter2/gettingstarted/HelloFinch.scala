package org.restwithscala.chapter2.gettingstarted

import io.finch._
import com.twitter.finagle.Http

object HelloFinch extends App {

  val app = get("hello"){ Ok("Hello, Finch!") }

  Http.serve(":8080", app.toService)

  println("Press <enter> to exit.")
  Console.in.read.toChar
}
