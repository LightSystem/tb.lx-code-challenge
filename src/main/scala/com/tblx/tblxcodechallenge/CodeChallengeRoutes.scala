package com.tblx.tblxcodechallenge

import cats.data.Kleisli
import cats.effect._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Request, Response}

object CodeChallengeRoutes {

  val codeChallengeRoutes: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "status" => IO(Response(Ok))
    case req @ POST -> Root / "operators" / "list" => DublinBus.runningOperators(req)
    case req @ POST -> Root / "vehicles" / "list" => ???
    case req @ POST -> Root / "vehicles" / "at-stop" => ???
    case req @ POST -> Root / "vehicle" / "trace" => ???
  }.orNotFound

}