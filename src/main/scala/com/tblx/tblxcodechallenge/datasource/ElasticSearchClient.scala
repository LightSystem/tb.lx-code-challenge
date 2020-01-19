package com.tblx.tblxcodechallenge.datasource

import cats.effect.IO
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s._
import com.sksamuel.elastic4s.cats.effect.instances._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.searches.{SearchRequest, SearchResponse}
import com.tblx.tblxcodechallenge.apimodel._
import com.tblx.tblxcodechallenge.datasource.DublinBusHitReaders._
import org.slf4j.LoggerFactory

object ElasticSearchClient {

  private val logger = LoggerFactory.getLogger(getClass)
  private lazy val client = ElasticClient(JavaClient(ElasticProperties("http://localhost:9200")))

  private val indexName = "dublin_bus"

  def runningOperators(request: RunningOperatorsRequest): IO[RunningOperatorsResponse] = {
    val aggName = "uniq_operator"

    val query = search(indexName)
      .size(0)
      .query(timeFrameQuery(request))
      .aggregations(termsAgg(aggName, "operator"))

    client.execute(query).map {
      case failure: RequestFailure => handleFailure(query, failure)
      case response: RequestSuccess[SearchResponse] =>
        val operators = extractAggResult[String](response, aggName).toSet
        RunningOperatorsResponse(request, operators)
    }
  }

  def vehicleIDs(request: VehicleIDsRequest): IO[VehicleIDsResponse] = {
    val aggName = "uniq_vehicle"

    val filter = {
      val base = Seq(
        timeFrameQuery(request),
        termQuery("operator", request.operator)
      )
      request.atStop.map(atStop => base :+ termQuery("at_stop", atStop)).getOrElse(base)
    }

    val query = search(indexName)
      .size(0)
      .query(bool(filter, Seq.empty, Seq.empty))
      .aggregations(termsAgg(aggName, "vehicle_id"))

    client.execute(query).map {
      case failure: RequestFailure => handleFailure(query, failure)
      case response: RequestSuccess[SearchResponse] =>
        val vehicleIDs = extractAggResult[Int](response, aggName).toSet
        VehicleIDsResponse(request, vehicleIDs)
    }
  }

  def vehicleTrace(request: VehicleTraceRequest): IO[VehicleTraceResponse] = {
    val query = search(indexName)
      .size(100)
      .query(bool(
        Seq(timeFrameQuery(request), termQuery("vehicle_id", request.vehicleID)),
        Seq.empty, Seq.empty
      ))
      .sortByFieldAsc("@timestamp")
    client.execute(query).map {
      case failure: RequestFailure => handleFailure(query, failure)
      case response: RequestSuccess[SearchResponse] =>
        val vehicleTrace = response.result.to[VehicleTrace]
        VehicleTraceResponse(request, vehicleTrace)
    }
  }

  // private functions below

  private def timeFrameQuery(request: DublinBusRequest) = {
    rangeQuery("@timestamp")
      .gte(ElasticDate(request.startTime.toString))
      .lte(ElasticDate(request.endTime.toString))
  }

  private def extractAggResult[T](response: RequestSuccess[SearchResponse], aggName: String) = {
    response.result.aggs
      .data(aggName)
      .asInstanceOf[Map[String, List[Map[String, T]]]]
      .apply("buckets")
      .map(_.apply("key"))
  }

  private def handleFailure(query: SearchRequest, failure: RequestFailure) = {
    logger.error("ElasticSearch query failed", failure.error.asException)
    logger.debug(s"Query: ${client.show(query)}")
    throw failure.error.asException
  }
}
