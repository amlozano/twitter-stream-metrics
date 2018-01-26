lazy val defaultScalaOptions = Seq(
  scalaVersion in ThisBuild := "2.11.8"
)

lazy val twitterIngestion = Project(
  id = "twitter-ingestion",
  base = file("twitter-ingestion"),
  settings = defaultScalaOptions
)

lazy val aggregator = Project(
  id = "aggregator",
  base = file("aggregator"),
  settings = defaultScalaOptions
)

lazy val kafkaToInfluxdb = Project(
  id = "kafka-to-influxdb",
  base = file("kafka-to-influxdb"),
  settings = defaultScalaOptions
)