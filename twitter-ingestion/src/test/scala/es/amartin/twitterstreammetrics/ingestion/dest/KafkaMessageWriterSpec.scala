package es.amartin.twitterstreammetrics.ingestion.dest

import es.amartin.twitterstreammetrics.ingestion.config.Configuration
import es.amartin.twitterstreammetrics.ingestion.dest.config.ConfigurationStub
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.Span
import org.scalatest.{FunSpec, Matchers}

class KafkaMessageWriterSpec extends FunSpec with Matchers with EmbeddedKafka with Eventually {


  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(10, org.scalatest.time.Seconds)),
    interval = scaled(Span(10, org.scalatest.time.Millis)))

  val userDefinedConfig = EmbeddedKafkaConfig(kafkaPort = 0, zooKeeperPort = 0)

  private def fixture(kafkaPort: Int, zookeeperPort: Int) = new {
    implicit val config: Configuration = new ConfigurationStub(kafkaPort, zookeeperPort)
    val recordGenerator = new MessageToOnlyByteValueRecordGenerator(config.kafkaTopic)
    val unit = new KafkaMessageWriter(recordGenerator)
  }

  describe("A KafkaMessageWriter") {

    it("should write one message to kafka") {
      withRunningKafkaOnFoundPort(userDefinedConfig) { implicit actualConfig =>
        val f = fixture(kafkaPort = actualConfig.kafkaPort, zookeeperPort = actualConfig.zooKeeperPort)
        val message = "Any message to send"
        f.unit.write(message)
        eventually {
          consumeFirstStringMessageFrom(f.config.kafkaTopic) shouldBe message
        }
      }
    }
  }

}
