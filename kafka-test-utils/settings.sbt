name := "kafka-test-utils"
version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  Dependencies.config,
  Dependencies.scalaLogging,
  Dependencies.logback,
  Dependencies.dockerTestKit,
  Dependencies.dockerTestKitImpl,
  Dependencies.kafkaStreams,
  Dependencies.Test.scalatest
)