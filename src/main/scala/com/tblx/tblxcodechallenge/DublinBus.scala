package com.tblx.tblxcodechallenge

import cats.effect.IO
import com.tblx.tblxcodechallenge.apimodel.RequestDecoders._
import com.tblx.tblxcodechallenge.apimodel.{AtStopRequest, RunningOperatorsRequest, RunningOperatorsResponse, VehicleIDsRequest, VehicleTraceRequest}
import com.tblx.tblxcodechallenge.datasource.ElasticSearchClient
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{Request, Response}
import org.http4s.circe._
import org.http4s.dsl.io._

object DublinBus {
  def runningOperators(req: Request[IO]): IO[Response[IO]] = for {
    reqParsed <- req.as[RunningOperatorsRequest]
    resp <- ElasticSearchClient.runningOperators(reqParsed)
    encodedResp <- Ok(resp.asJson)
  } yield encodedResp

  def vehicleIDs(req: Request[IO]) = req.as[VehicleIDsRequest]

  def atStop(req: Request[IO]) = req.as[AtStopRequest]

  def vehicleTrace(req: Request[IO]) = req.as[VehicleTraceRequest]
}
