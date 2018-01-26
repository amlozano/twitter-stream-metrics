name := "aggregator"
version := "0.1-SNAPSHOT"

assemblyJarName in assembly := "aggregator.jar"
assemblyMergeStrategy in assembly := {
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
  Dependencies.Test.scalatest,
  Dependencies.Test.embeddedKafka
)