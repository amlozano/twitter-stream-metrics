import sbt._

object Dependencies {
  private val jacksonVersion = "2.9.9"
  private val twitterVersion = "19.7.0"
  private val twitterHbcVersion = "2.2.0"
  private val kafkaStreamsVersion = "2.3.0"
  private val gsonVersion = "2.8.5"
  private val configVersion = "1.3.4"
  private val scalaLoggingVersion = "3.9.2"
  private val scalatestVersion = "3.0.8"
  private val logbackVersion = "1.2.3"
  private val influxVersion = "2.15"
  private val dockerTestKitVersion = "0.9.9"

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
  val dockerTestKit = "com.whisk" %% "docker-testkit-scalatest" % dockerTestKitVersion
  val dockerTestKitImpl = "com.whisk" %% "docker-testkit-impl-spotify" % dockerTestKitVersion

  object Test {
    val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  }

}