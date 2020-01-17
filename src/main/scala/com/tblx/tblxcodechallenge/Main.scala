package com.tblx.tblxcodechallenge

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] ={
    val httpApp = Logger.httpApp(logHeaders = true, logBody = true)(CodeChallengeRoutes.codeChallengeRoutes)

    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}