package es.amartin.twitterstreammetrics.toinflux.influx

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, AggregationValue, HistogramPoint}
import org.influxdb.dto.Point
import org.scalatest.{FunSpec, Matchers}

class InfluxKeyValWriterImplSpec extends FunSpec with Matchers {

  private def fixture(expectedDatabase: String, expectedRetentionPolicy: String, expectedPoints: List[Point]) = new {
    val mockClient = new InfluxDBClientStub(expectedDatabase, expectedRetentionPolicy, expectedPoints)
    val unit = new InfluxKeyValWriterImpl(mockClient, new KeyValToPointConverterImpl)
  }

  describe("A InfluxKeyValWriterImpl should") {

    it("write several batch points to the specified database and with the specified retention policy") {
      val database = "test"
      val retentionPolicy = "autogen"
      val batch = List(
        (AggregationKey("function1", "aggregationName1", Map[String, String]()),
          HistogramPoint(1, 2, AggregationValue(1, "function1"))),
        (AggregationKey("function2", "aggregationName1", Map[String, String]()),
          HistogramPoint(2, 3, AggregationValue(3, "function2"))),
        (AggregationKey("function1", "aggregationname2", Map[String, String]("location" -> "here")),
          HistogramPoint(3, 4, AggregationValue(20, "function1"))))
      val expectedPoints = (0 to 2).map(i => new KeyValToPointConverterImpl().convert(batch(i)._1, batch(i)._2)).toList

      val f = fixture(database, retentionPolicy, expectedPoints)

      f.unit.writeItems(batch, database, retentionPolicy)
    }
  }
}
