package com.tblx.tblxcodechallenge.datasource

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.sksamuel.elastic4s.{Hit, HitReader}
import com.tblx.tblxcodechallenge.apimodel.VehicleTrace

import scala.util.Try

object DublinBusHitReaders {
  implicit object VehicleTraceHitReader extends HitReader[VehicleTrace] {
    override def read(hit: Hit): Try[VehicleTrace] = {
      Try(VehicleTrace(
        hit.sourceField("latitude").toString,
        hit.sourceField("longitude").toString,
        LocalDateTime.parse(hit.sourceField("@timestamp").toString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
      ))
    }
  }
}
