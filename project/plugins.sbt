val sbtSmrtsVersion = "1.3.37"

addSbtPlugin("com.salesforceiq.intelligence" % "sbt-smrts-core" % sbtSmrtsVersion)
addSbtPlugin("com.salesforceiq.intelligence" % "sbt-smrts-databricks" % sbtSmrtsVersion)

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.7")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.5")
