package es.amartin.twitterstreammetrics.kafka

import java.time.Duration
import java.util.{Properties, UUID}

import com.whisk.docker.impl.spotify.DockerKitSpotify
import com.whisk.docker.{DockerContainer, DockerReadyChecker}
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.Serde

import scala.collection.JavaConverters._

trait DockerKafkaService[K, V] extends DockerKitSpotify {
  val DefaultKafkaPort = 9092
  val DefaultZookeeperPort = 2181
  val zkSessionTimeoutMs = 10000
  val zkConnectionTimeoutMs = 10000

  val keySerde: Serde[K]
  val valueSerde: Serde[V]
  implicit val kafkaConfig: KafkaConfiguration

  private val zookeeperContainer: DockerContainer = DockerContainer("wurstmeister/zookeeper")
    .withPorts(DefaultZookeeperPort -> Some(DefaultZookeeperPort))
    .withReadyChecker(DockerReadyChecker.LogLineContains("binding to port"))

  val kafkaContainer: DockerContainer = DockerContainer("wurstmeister/kafka")
    .withPorts(DefaultKafkaPort -> Some(DefaultKafkaPort))
    .withEnv(s"ADVERTISED_PORT=$DefaultKafkaPort", s"ADVERTISED_HOST=${dockerExecutor.host}")
    .withReadyChecker(DockerReadyChecker.LogLineContains("kafka entered RUNNING state"))

  override def dockerContainers: List[DockerContainer] = zookeeperContainer :: kafkaContainer :: super.dockerContainers

  val kafkaAdminClient: AdminClient = AdminClient.create(Map[String, Object](
    AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG -> s"${dockerExecutor.host}:$DefaultKafkaPort",
    AdminClientConfig.CLIENT_ID_CONFIG -> "test-kafka-admin-client",
    AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG -> zkSessionTimeoutMs.toString,
    AdminClientConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG -> zkConnectionTimeoutMs.toString
  ).asJava)

  private val consumerProperties = new Properties
  consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig.offsetResetConfig)
  consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.brokers)
  consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
  private val kafkaConsumer = new KafkaConsumer[K, V](consumerProperties, keySerde.deserializer(), valueSerde.deserializer())
  kafkaConsumer.subscribe(List(kafkaConfig.topic).asJava)

  private val producerProperties = new Properties
  producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.brokers)
  producerProperties.put(ProducerConfig.ACKS_CONFIG, "all")

  private val kafkaProducer = new KafkaProducer[K, V](producerProperties, keySerde.serializer(), valueSerde.serializer())

  def consumeFirstMessage(): V = {
    kafkaConsumer.poll(Duration.ofSeconds(30)).iterator.next.value
  }

}
