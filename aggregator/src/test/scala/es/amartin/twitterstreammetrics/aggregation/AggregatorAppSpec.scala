package es.amartin.twitterstreammetrics.aggregation

import es.amartin.twitterstreammetrics.aggregation.aggregation.{MetricDefinition, MetricDefinitionRetriever,
PredefinedMetricDefinitions}
import es.amartin.twitterstreammetrics.aggregation.config.{Configuration, ConfigurationStub}
import es.amartin.twitterstreammetrics.aggregation.io.{KafkaStream, TwitterMetricKafkaStreamWriter}
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.Span

import scala.concurrent.duration._

class AggregatorAppSpec extends FunSpec with Matchers with EmbeddedKafka with Eventually {

  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(50, org.scalatest.time.Seconds)),
    interval = scaled(Span(1, org.scalatest.time.Seconds)))

  val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

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
      withRunningKafkaOnFoundPort(userDefinedConfig) { implicit actualConfig =>
        val f = fixture(kafkaPort = actualConfig.kafkaPort, zookeeperPort = actualConfig.zooKeeperPort)
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

}
