package com.tblx.tblxcodechallenge

import cats.effect.IO
import org.http4s.{Method, Request, Response, _}

class DublinBusSpec extends org.specs2.mutable.Specification {
  "DublinBus" >> {
    "RunningOperatorsRequest" >> {
      "throw MalformedMessageBodyFailure exception on empty body" >> {
        emptyBodyRequest must throwA[MalformedMessageBodyFailure]
      }
    }
  }

  private def emptyBodyRequest: Response[IO] = {
    val req = Request[IO](Method.POST, uri"/operators/list")
    DublinBus.runningOperators(req).unsafeRunSync()
  }

}
