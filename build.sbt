lazy val defaultScalaOptions = Seq(
  ThisBuild / scalaVersion := "2.12.8"
)

lazy val root = (project in file("."))
  .aggregate(kafkaUtils, kafkaTestUtils, twitterIngestion, aggregator, kafkaToInfluxdb)
  .settings(
    aggregate in update := false
  )

lazy val kafkaUtils = Project(
  id = "kafka-utils",
  base = file("kafka-utils")
).settings(defaultScalaOptions)

lazy val kafkaTestUtils = Project(
  id = "kafka-test-utils",
  base = file("kafka-test-utils"))
  .settings(defaultScalaOptions)
  .dependsOn(kafkaUtils % "compile->compile;test->test")

lazy val twitterIngestion = Project(
  id = "twitter-ingestion",
  base = file("twitter-ingestion"))
  .settings(defaultScalaOptions)
  .dependsOn(kafkaUtils % "compile->compile;test->test")
  .dependsOn(kafkaTestUtils % "test->compile")

lazy val aggregator = Project(
  id = "aggregator",
  base = file("aggregator")
).settings(defaultScalaOptions)

lazy val kafkaToInfluxdb = Project(
  id = "kafka-to-influxdb",
  base = file("kafka-to-influxdb"))
  .settings(defaultScalaOptions)
  .dependsOn(kafkaUtils % "compile->compile;test->test")