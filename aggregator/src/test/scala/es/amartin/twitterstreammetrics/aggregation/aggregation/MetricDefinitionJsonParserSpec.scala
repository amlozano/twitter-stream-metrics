package es.amartin.twitterstreammetrics.aggregation.aggregation

import org.scalatest.{FunSpec, Matchers}

class MetricDefinitionJsonParserSpec extends FunSpec with Matchers {

  private def fixture = new {
    val jsonText = """{"key1":"value1", "key2": {"nestedKey2": "value2"}}"""
    val metricDefinitionWithIfHasTrue: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"key1":["value1"]},
        |"ifHas":"key2.nestedKey2"
        |}""".stripMargin
    val metricDefinitionWithIfHasNotTrue: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"key1":["value1"]},
        |"ifHasNot":"key2.something"
        |}""".stripMargin
    val metricDefinitionWithIfHasFalse: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"key1":["value1"]},
        |"ifHas":"key2.something"
        |}""".stripMargin
    val metricDefinitionWithIfHasNotFalse: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"key1":["value1"]},
        |"ifHasNot":"key2.nestedKey2"
        |}""".stripMargin
    val metricDefinitionWithoutAny: String =
      """{
        |"name":"name",
        |"groupBy":["groupBy","andBy"],
        |"textFilters":{"key1":["value1"]}
        |}""".stripMargin
    val unit = new MetricDefinitionJsonParser
  }

  describe("A MetricDefinitionJsonParserSpec") {

    it("should parse and configure proper filter for ifHas with true result") {
      val f = fixture

      val result = f.unit.parse(f.metricDefinitionWithIfHasTrue)

      result.filter(JsonUtils.toJsonNode(f.jsonText)) shouldBe true
    }

    it("should parse and configure proper filter for ifHas with false result") {
      val f = fixture

      val result = f.unit.parse(f.metricDefinitionWithIfHasFalse)

      result.filter(JsonUtils.toJsonNode(f.jsonText)) shouldBe false
    }

    it("should parse and configure proper filter for ifHasNot with true result") {
      val f = fixture

      val result = f.unit.parse(f.metricDefinitionWithIfHasNotTrue)

      result.filter(JsonUtils.toJsonNode(f.jsonText)) shouldBe true
    }

    it("should parse and configure proper filter for ifHasNot with false result") {
      val f = fixture

      val result = f.unit.parse(f.metricDefinitionWithIfHasNotFalse)

      result.filter(JsonUtils.toJsonNode(f.jsonText)) shouldBe false
    }

    it("should parse and configure proper filter for definition without ifHas or ifHasNot") {
      val f = fixture

      val result = f.unit.parse(f.metricDefinitionWithoutAny)

      result.filter(JsonUtils.toJsonNode(f.jsonText)) shouldBe true
    }
  }

}
