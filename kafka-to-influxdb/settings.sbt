name := "kafka-to-influxdb"
version := "0.1-SNAPSHOT"

assembly / assemblyJarName := "kafka-to-influxdb.jar"
assembly / assemblyMergeStrategy := {
  case "application.conf" => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

libraryDependencies ++= Seq(
  Dependencies.config,
  Dependencies.gson,
  Dependencies.jackson,
  Dependencies.kafkaStreams,
  Dependencies.scalaLogging,
  Dependencies.logback,
  Dependencies.influxDbScalaClient,
  Dependencies.Test.scalatest
)