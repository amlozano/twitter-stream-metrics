package es.amartin.twitterstreammetrics.aggregation.aggregation

import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

class CachedMetricDefinitionRetrieverSpec extends FunSpec with Matchers with Eventually {


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(1, Seconds))

  def fixture(results: List[String], duration: Duration) = new {
    val unit = new CachedMetricDefinitionRetriever(new MetricDefinitionRetrieverStub(results), duration)
  }

  describe("A CachedMetricDefinitionRetriever") {
    it("should return the cached element if cache didn't expire") {
      val f = fixture(List("first", "second"), 20.minutes)

      f.unit.get.head.name shouldBe "first"
      f.unit.get.head.name shouldBe "first"
    }

    it("should return the second element if cache expired") {
      val f = fixture(List("first", "second"), 2.seconds)

      f.unit.get.head.name shouldBe "first"
      eventually {
        f.unit.get.head.name shouldBe "second"
      }
    }
  }

}
