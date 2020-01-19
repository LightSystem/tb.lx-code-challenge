package com.tblx.tblxcodechallenge.apimodel

import java.time.LocalDateTime

sealed trait DublinBusResponse[T <: DublinBusRequest] {
  val request: T
}
final case class RunningOperatorsResponse(request: RunningOperatorsRequest, operators: Set[String]) extends DublinBusResponse[RunningOperatorsRequest]
final case class VehicleIDsResponse(request: VehicleIDsRequest, vehicleIDs: Set[Int]) extends DublinBusResponse[VehicleIDsRequest]
final case class VehicleTraceResponse(request: VehicleTraceRequest, trace: Seq[VehicleTrace])

final case class VehicleTrace(lat: String, lon: String, timestamp: LocalDateTime)