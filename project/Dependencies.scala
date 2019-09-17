import sbt._

object Dependencies {
  val scalatestVersion = "3.0.8"
  val scalacheckVersion = "1.14.0"

  val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion
  val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion

  object spark {
    val version = "2.4.3"
    val org = "org.apache.spark"

    val core = org %% "spark-core" % version % Provided
    val sql = org %% "spark-sql" % version % Provided
    val ml = org %% "spark-mllib" % version % Provided
  }

  object alchemy {
    val org = "com.salesforceiq"
    val version = "2.5.256"

    val smrtTools = org %% "alchemy-smrt-tools" % version
  }

  object smrtTestkit {
    val org = "com.salesforceiq.smrt"
    val version = "0.1.9"

    val core = org %% "testkit-core" % version
    val spark = org %% "testkit-spark" % version
  }
}
