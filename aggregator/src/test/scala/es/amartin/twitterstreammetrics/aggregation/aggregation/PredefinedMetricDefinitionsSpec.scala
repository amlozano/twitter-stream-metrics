package es.amartin.twitterstreammetrics.aggregation.aggregation

import org.scalatest.{FunSpec, Matchers}

class PredefinedMetricDefinitionsSpec extends FunSpec with Matchers {

  private def fixture = new {
    val jsonMessage = s"""{"text":"elem1", "groupingKey": {"nested":"groupingValue"}}"""
    val unit = PredefinedMetricDefinitions
  }

  describe("A PredefinedMetricDefinitions") {
    describe("when count") {
      it("filter should return true when filterWords are present") {
        val f = fixture

        val definition = f.unit.count(textFilters = Map("text" -> Set("elem1", "elem2")), name = "name")

        definition.filter(JsonUtils.toJsonNode(f.jsonMessage)) shouldBe true
      }

      it("filter should return false when filterWords are not present") {
        val f = fixture

        val definition = f.unit.count(textFilters = Map("text"-> Set("elem3")), name = "name")

        definition.filter(JsonUtils.toJsonNode(f.jsonMessage)) shouldBe false
      }

      it("filter should return true for an empty filterWords") {
        val f = fixture

        val definition = f.unit.count("name")

        definition.filter(JsonUtils.toJsonNode(f.jsonMessage)) shouldBe true
      }

      it("fieldExctactor should return empty map when no paths to group by are present") {
        val f = fixture

        val definition = f.unit.count(textFilters = Map("text" -> Set("elem1", "elem2")), name = "name")

        definition.fieldExtractor(JsonUtils.toJsonNode(f.jsonMessage)) shouldBe Map()
      }

      it("fieldExtractor should return a correct mapping when path to group by are present") {
        val f = fixture

        val definition = f.unit
          .count(textFilters = Map("text"-> Set("elem1", "elem2")),
            name = "name",
            pathsGroupBy = Set("groupingKey.nested"))

        definition
          .fieldExtractor(JsonUtils.toJsonNode(f.jsonMessage)) shouldBe Map("groupingKey.nested" -> "groupingValue")
      }
    }
  }
}
