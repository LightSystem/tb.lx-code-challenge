package com.tblx.tblxcodechallenge.apimodel

import java.time.LocalDateTime

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.EntityDecoder
import org.http4s.circe._

sealed trait DublinBusRequest {
  val startTime: LocalDateTime
  val endTime: LocalDateTime
}
final case class RunningOperatorsRequest(startTime: LocalDateTime, endTime: LocalDateTime) extends DublinBusRequest
final case class VehicleIDsRequest(startTime: LocalDateTime, endTime: LocalDateTime, operator: String, atStop: Option[Boolean]) extends DublinBusRequest
final case class VehicleTraceRequest(startTime: LocalDateTime, endTime: LocalDateTime, vehicleID: Int) extends DublinBusRequest

object RequestDecoders {
  implicit val runningOperatorsRequestDecoder: EntityDecoder[IO, RunningOperatorsRequest] = jsonOf[IO, RunningOperatorsRequest]
  implicit val vehicleIDsRequestDecoder: EntityDecoder[IO, VehicleIDsRequest] = jsonOf[IO, VehicleIDsRequest]
  implicit val vehicleTraceRequestDecoder: EntityDecoder[IO, VehicleTraceRequest] = jsonOf[IO, VehicleTraceRequest]
}