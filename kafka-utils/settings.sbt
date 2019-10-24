name := "kafka-utils"
version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  Dependencies.config,
  Dependencies.scalaLogging,
  Dependencies.logback,
  Dependencies.kafkaStreams,
  Dependencies.jackson
)