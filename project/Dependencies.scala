import sbt._

object Dependencies {
  private val jacksonVersion = "2.8.7"
  private val twitterVersion = "6.34.0"
  private val twitterHbcVersion = "2.2.0"
  private val kafkaStreamsVersion = "0.11.0.0"
  private val gsonVersion = "2.7"
  private val configVersion = "1.0.1"
  private val scalaLoggingVersion = "3.4.0"
  private val scalatestVersion = "3.0.1"
  private val logbackVersion = "1.2.3"
  private val embeddedKafkaVersion = "0.15.0"
  private val influxVersion = "2.3"

  val jackson = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
  val twitter = "com.twitter" %% "util-core" % twitterVersion
  val kafkaStreams = "org.apache.kafka" % "kafka-streams" %
    kafkaStreamsVersion exclude("org.slf4j", "slf4j-log4j12") exclude("javax.jms", "jms") exclude("com.sun.jdmk",
    "jmxtools") exclude("com.sun.jmx", "jmxri")
  val gson = "com.google.code.gson" % "gson" % gsonVersion
  val config = "com.typesafe" % "config" % configVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  val twitterHbc = "com.twitter" % "hbc-core" % twitterHbcVersion
  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  val influxDbScalaClient = "org.influxdb" % "influxdb-java" % influxVersion

  object Test {
    val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % embeddedKafkaVersion % "test"
  }

}