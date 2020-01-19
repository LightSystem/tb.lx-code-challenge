package com.tblx.tblxcodechallenge

import cats.effect.IO
import com.tblx.tblxcodechallenge.apimodel.RequestDecoders._
import com.tblx.tblxcodechallenge.apimodel.{RunningOperatorsRequest, VehicleIDsRequest, VehicleTraceRequest}
import com.tblx.tblxcodechallenge.datasource.ElasticSearchClient
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.{Request, Response}

object DublinBus {
  def runningOperators(req: Request[IO]): IO[Response[IO]] = for {
    reqParsed <- req.as[RunningOperatorsRequest]
    resp <- ElasticSearchClient.runningOperators(reqParsed)
    encodedResp <- Ok(resp.asJson)
  } yield encodedResp

  def vehicleIDs(req: Request[IO]): IO[Response[IO]] = for {
    reqParsed <- req.as[VehicleIDsRequest]
    resp <- ElasticSearchClient.vehicleIDs(reqParsed)
    encodedResp <- Ok(resp.asJson)
  } yield encodedResp

  def vehicleTrace(req: Request[IO]): IO[Response[IO]] = for {
    reqParsed <- req.as[VehicleTraceRequest]
    resp <- ElasticSearchClient.vehicleTrace(reqParsed)
    encodedResp <- Ok(resp.asJson)
  } yield encodedResp
}
