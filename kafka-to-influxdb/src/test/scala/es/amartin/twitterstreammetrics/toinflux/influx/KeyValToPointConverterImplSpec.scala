package es.amartin.twitterstreammetrics.toinflux.influx

import es.amartin.twitterstreammetrics.toinflux.data.{AggregationKey, AggregationValue, HistogramPoint}
import org.scalatest.{FunSpec, Matchers}

class KeyValToPointConverterImplSpec extends FunSpec with Matchers {

  private def fixture = new {
    val key = AggregationKey("aggregationFunction", "name", Map[String, String]("fieldName" -> "fieldValue"))
    val value = HistogramPoint(1, 2, AggregationValue(20, "valueFunction"))
    val unit = new KeyValToPointConverterImpl
  }

  describe("A KeyValToPointConverterImpl") {
    it("should add as tags the fields from a key") {
      val f = fixture

      val result = f.unit.convert(f.key, f.value)

      result.toString.contains("tags={fieldName=fieldValue}") shouldBe true
    }

    it("should add as name the aggregation name") {
      val f = fixture

      val result = f.unit.convert(f.key, f.value)

      result.toString.contains("name=name") shouldBe true
    }

    it("should add as fields the aggregation value function and its value") {
      val f = fixture

      val result = f.unit.convert(f.key, f.value)

      result.toString.contains("fields={valueFunction=20}") shouldBe true
    }

    it("should add as time the starting point of the histogram point") {
      val f = fixture

      val result = f.unit.convert(f.key, f.value)

      result.toString.contains("time=1") shouldBe true
    }
  }
}
