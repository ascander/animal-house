package com.salesforceiq.animalhouse

import cats.implicits._
import com.salesforceiq.smrt.testkit.SmrtSpec
import com.salesforceiq.smrt.testkit.spark.TestSparkSession

import org.apache.spark.sql.functions.avg

class AnimalHouseSpec extends SmrtSpec with TestSparkSession {
  test("test spark session can be used") {
    import spark.implicits._
    val ds = spark.createDataset(1 to 9)
    val dsAvg = ds.agg(avg(ds("value"))).as[Double].first // 😱
    val expected = 5.0
    dsAvg should ===(expected)
  }
}
