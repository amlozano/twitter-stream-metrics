name := "twitter-ingestion"
version := "0.1-SNAPSHOT"

assemblyJarName in assembly := "twitter-ingestion.jar"
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
  Dependencies.twitter,
  Dependencies.twitterHbc,
  Dependencies.logback,
  Dependencies.Test.scalatest
)