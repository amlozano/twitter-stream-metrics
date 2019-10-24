package es.amartin.twitterstreammetrics.aggregation

import es.amartin.twitterstreammetrics.aggregation.aggregation.{MetricDefinition, MetricDefinitionRetriever,
PredefinedMetricDefinitions}
import es.amartin.twitterstreammetrics.aggregation.config.{Configuration, ConfigurationStub}
import es.amartin.twitterstreammetrics.aggregation.io.{KafkaStream, TwitterMetricKafkaStreamWriter}
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.Span

import scala.concurrent.duration._

class AggregatorAppSpec extends FunSpec with Matchers with KafkaTesting with Eventually {

  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(50, org.scalatest.time.Seconds)),
    interval = scaled(Span(1, org.scalatest.time.Seconds)))

  private def fixture(kafkaPort: Int, zookeeperPort: Int) = new {
    implicit val config: Configuration = new ConfigurationStub(kafkaPort, zookeeperPort)
    val metricDefinitionRetriever = new MetricDefinitionRetriever {
      override def get: Set[MetricDefinition] = Set(
        PredefinedMetricDefinitions
          .count(textFilters = Map("text" -> Set("malaga", "málaga")), name = "containsMalaga")
      )
    }
    val kafkaStream = new KafkaStream
    val unit = new TwitterMetricKafkaStreamWriter(metricDefinitionRetriever, config.kafkaOutputTopic, 10.seconds)
  }

  describe("An AggregatorApp") {

    it("should generate a metric for a message that match the filter") {
        val f = fixture(kafkaPort = 9092, zookeeperPort = 2181)
        val message = MessageGenerator.generateMalagaMessage
        publishStringMessageToKafka(f.config.kafkaInputTopic, message)
        publishStringMessageToKafka(f.config.kafkaInputTopic, message)

        f.unit.write(f.kafkaStream.inputStream)

        f.kafkaStream.stream.start()
        eventually {
          val messages = consumeNumberStringMessagesFrom(f.config.kafkaOutputTopic, 1)
          messages.head should contain
          """"value":{"value":2,
            |"aggregationFunction":"count"}}""".stripMargin
        }

    }
  }

}
