package es.amartin.twitterstreammetrics.toinflux.influx

import org.influxdb.dto.Point
import org.scalatest.Matchers

class InfluxDBClientStub(expectedDatabase: String, expectedRetentionPolicy: String, expectedPoints: List[Point])
  extends InfluxDBClient with Matchers {

  var writtenElements = 0

  private def shouldEqual(point1: Point, point2: Point): Unit = {
    point1.toString shouldBe point2.toString
  }

  private def shouldEqual(points1: List[Point], points2: List[Point]): Unit = {
    points1.size shouldBe points2.size
    points1.indices foreach { index =>
      shouldEqual(points1(index), points2(index))
    }
  }

  override def writeItems(
    database: String,
    retentionPolicy: String,
    points: Iterable[Point]): Unit = {

    database shouldBe expectedDatabase
    retentionPolicy shouldBe expectedRetentionPolicy
    expectedPoints.lengthCompare(writtenElements) shouldBe 1
    shouldEqual(points.toList, expectedPoints.slice(writtenElements, writtenElements + points.size))

    writtenElements += points.size
  }
}
