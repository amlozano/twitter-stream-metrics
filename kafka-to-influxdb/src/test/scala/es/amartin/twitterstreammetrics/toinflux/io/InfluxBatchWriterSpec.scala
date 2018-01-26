package es.amartin.twitterstreammetrics.toinflux.io

import es.amartin.twitterstreammetrics.toinflux.config.{Configuration, ConfigurationStub}
import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.serdes.JSONSerializer
import org.scalatest.{FunSpec, Matchers}

class InfluxBatchWriterSpec extends FunSpec with Matchers {

  private def fixture(batchSize: Option[Int] = None) = new {
    implicit val config: Configuration = new ConfigurationStub
    val keyValWriterStub = new InfluxKeyValWriterStub(config.influxDatabase, config.influxRetentionPolicy)
    val unit = new InfluxBatchWriter(keyValWriterStub,
      config.influxDatabase,
      config.influxRetentionPolicy,
      batchSize.getOrElse(config.influxBatchSize))
  }

  describe("an InfluxBatchWriter should") {
    it("write a batch with a single item from kafka with correct database and retention policy") {
      implicit val (keySerializer, serializer) =
        (new JSONSerializer[AggregationKey], new JSONSerializer[HistogramPoint])
      val f = fixture()
      val (messageKey, messageValue) = MetricGenerator.generateMetric("count", "test", Map(), 1, 2, 2)

      f.keyValWriterStub.writtenElements.isEmpty shouldBe true
      f.keyValWriterStub.madeCalls shouldBe 0

      f.unit.writeItems(List((messageKey, messageValue)))

      f.keyValWriterStub.writtenElements.size shouldBe 1
      f.keyValWriterStub.writtenElements.get(messageKey) shouldBe Some(messageValue)

    }

    it(
      "write with two batches when elements that exceeds batchSize from kafka with correct database and retention " +
        "policy") {
      implicit val (keySerializer, serializer) =
        (new JSONSerializer[AggregationKey], new JSONSerializer[HistogramPoint])
      val f = fixture(batchSize = Some(1))
      val (messageKey1, messageValue1) = MetricGenerator.generateMetric("count", "test", Map(), 1, 2, 2)
      val (messageKey2, messageValue2) = MetricGenerator.generateMetric("count", "test2", Map(), 2, 3, 4)

      f.keyValWriterStub.writtenElements.isEmpty shouldBe true
      f.keyValWriterStub.madeCalls shouldBe 0

      f.unit.writeItems(List((messageKey1, messageValue1), (messageKey2, messageValue2)))

      f.keyValWriterStub.writtenElements.size shouldBe 2
      f.keyValWriterStub.madeCalls shouldBe 2
      f.keyValWriterStub.writtenElements.get(messageKey1) shouldBe Some(messageValue1)
      f.keyValWriterStub.writtenElements.get(messageKey2) shouldBe Some(messageValue2)
    }

    it("write a batch that doesn't exceeds batchSize from kafka with correct database and retention policy") {
      implicit val (keySerializer, serializer) =
        (new JSONSerializer[AggregationKey], new JSONSerializer[HistogramPoint])
      val f = fixture(batchSize = Some(3))
      val (messageKey1, messageValue1) = MetricGenerator.generateMetric("count", "test", Map(), 1, 2, 2)
      val (messageKey2, messageValue2) = MetricGenerator.generateMetric("count", "test2", Map(), 2, 3, 4)

      f.keyValWriterStub.writtenElements.isEmpty shouldBe true
      f.keyValWriterStub.madeCalls shouldBe 0

      f.unit.writeItems(List((messageKey1, messageValue1), (messageKey2, messageValue2)))

      f.keyValWriterStub.writtenElements.size shouldBe 2
      f.keyValWriterStub.madeCalls shouldBe 1
      f.keyValWriterStub.writtenElements.get(messageKey1) shouldBe Some(messageValue1)
      f.keyValWriterStub.writtenElements.get(messageKey2) shouldBe Some(messageValue2)

    }
  }

}
