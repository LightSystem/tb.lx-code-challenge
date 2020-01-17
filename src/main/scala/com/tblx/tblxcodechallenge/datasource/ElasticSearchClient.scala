package com.tblx.tblxcodechallenge.datasource

import cats.effect.IO
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.cats.effect.instances._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s._
import com.tblx.tblxcodechallenge.apimodel.{RunningOperatorsRequest, RunningOperatorsResponse}
import org.slf4j.LoggerFactory

object ElasticSearchClient {

  private val logger = LoggerFactory.getLogger(getClass)
  private lazy val client = ElasticClient(JavaClient(ElasticProperties("http://localhost:9200")))

  def runningOperators(request: RunningOperatorsRequest): IO[RunningOperatorsResponse] = {
    val query = search("dublin_bus")
      .size(0)
      .query(
        rangeQuery("@timestamp")
          .gte(ElasticDate(request.startTime.toString))
          .lte(ElasticDate(request.endTime.toString))
      )
      .aggregations(termsAgg("uniq_operator", "operator"))

    client.execute(query).map {
      case failure: RequestFailure =>
        logger.error("ElasticSearch query failed", failure.error.asException)
        logger.debug(s"Query: ${client.show(query)}")
        throw failure.error.asException
      case results: RequestSuccess[SearchResponse] => RunningOperatorsResponse(request, Seq.empty)
    }
  }
}
