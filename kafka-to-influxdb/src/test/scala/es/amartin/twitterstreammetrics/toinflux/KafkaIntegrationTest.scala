package es.amartin.twitterstreammetrics.toinflux

import es.amartin.twitterstreammetrics.toinflux.config.{Configuration, ConfigurationStub}
import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, HistogramPoint}
import es.amartin.twitterstreammetrics.toinflux.io.{InfluxBatchWriter, InfluxKeyValWriterStub, KafkaReader,
MetricGenerator}
import es.amartin.twitterstreammetrics.toinflux.serdes.{JSONSerde, JSONSerializer}
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.Span
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

class KafkaIntegrationTest extends FunSpec with Matchers with EmbeddedKafka with Eventually {


  implicit override val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(100, org.scalatest.time.Seconds)),
    interval = scaled(Span(10, org.scalatest.time.Millis)))

  val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

  private def fixture(kafkaPort: Int, zookeeperPort: Int, batchSize: Option[Int] = None) = new {
    implicit val config: Configuration = new ConfigurationStub(kafkaPort, zookeeperPort)
    val kafkaReader = new KafkaReader(new JSONSerde[AggregationKey], new JSONSerde[HistogramPoint])
    val keyValWriterStub = new InfluxKeyValWriterStub(config.influxDatabase, config.influxRetentionPolicy)
    val unit = new InfluxBatchWriter(keyValWriterStub,
      config.influxDatabase,
      config.influxRetentionPolicy,
      batchSize.getOrElse(config.influxBatchSize))
  }

  describe("Kafka-to-Influx should") {
    it("write a batch from kafka with correct database and retention policy") {
      withRunningKafkaOnFoundPort(userDefinedConfig) { implicit actualConfig =>
        implicit val (keySerializer, serializer) =
          (new JSONSerializer[AggregationKey], new JSONSerializer[HistogramPoint])
        val f = fixture(kafkaPort = actualConfig.kafkaPort, zookeeperPort = actualConfig.zooKeeperPort)
        val (messageKey, messageValue) = MetricGenerator.generateMetric("count", "test", Map(), 1, 2, 2)
        publishToKafka(f.config.kafkaTopic, messageKey, messageValue)

        f.keyValWriterStub.writtenElements.isEmpty shouldBe true
        f.keyValWriterStub.madeCalls shouldBe 0

        f.unit.writeItems(f.kafkaReader.readBatch(100.seconds.toMillis))
        eventually {
          f.keyValWriterStub.writtenElements.size shouldBe 1
          f.keyValWriterStub.writtenElements.get(messageKey) shouldBe Some(messageValue)
        }
      }
    }
  }

}
