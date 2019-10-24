package es.amartin.twitterstreammetrics.kafka.utils

import es.amartin.twitterstreammetrics.ingestion.config.Configuration
import es.amartin.twitterstreammetrics.kafka.utils.config.ConfigurationStub
import es.amartin.twitterstreammetrics.kafka.{DockerKafkaService, KafkaConfiguration, KafkaMessageWriter, MessageToOnlyByteValueRecordGenerator}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes.{ByteArraySerde, IntegerSerde}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.Span
import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}

class KafkaMessageWriterSpec
  extends FunSpec with DockerKafkaService[Integer, Array[Byte]] with Matchers with Eventually with BeforeAndAfterAll {
  override val keySerde: Serde[Integer] = new IntegerSerde
  override val valueSerde: Serde[Array[Byte]] = new ByteArraySerde
  override implicit val kafkaConfig: KafkaConfiguration = _

  override def beforeAll() {
    super.beforeAll()
    startAllOrFail()
  }

  override def afterAll() {
    stopAllQuietly()
    super.afterAll()
  }

  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(10, org.scalatest.time.Seconds)),
    interval = scaled(Span(10, org.scalatest.time.Millis)))

  private def fixture(kafkaPort: Int, zookeeperPort: Int) = new {
    implicit val config: Configuration = new ConfigurationStub(kafkaPort, zookeeperPort)
    val recordGenerator = new MessageToOnlyByteValueRecordGenerator(config.kafka.topic)
    val unit = new KafkaMessageWriter(recordGenerator, new IntegerSerde, new ByteArraySerde)
  }

  describe("A KafkaMessageWriter") {

    it("should write one message to kafka") {
      val f = fixture(kafkaPort = DefaultKafkaPort, zookeeperPort = DefaultZookeeperPort)
      val message = "Any message to send"
      f.unit.write(message)
      eventually {
          consumeFirstMessage().toString shouldBe message
      }
    }
  }
}
