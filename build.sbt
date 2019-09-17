import ReleaseTransformations._
import wartremover.Wart
import com.salesforceiq.intelligence.sbtsmrts.BasicSmrtsPlugin.forSideEffect

///////////////////////////////////////////////////////////
// Commands

val format = taskKey[Unit]("perform formatting of source files")
val lint = taskKey[Unit]("run linting of source files")

///////////////////////////////////////////////////////////
// Common build settings

lazy val commonSettings = Seq(
  organization := "com.salesforceiq",
  scalaVersion := "2.11.12",
  parallelExecution in Test := false,
  // This setting is needed to avoid success stacktraces in SBT 1.3.0
  // See: https://github.com/sbt/sbt/issues/5088
  fork in Test := true,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
  connectInput := true,
  updateOptions := updateOptions.value.withCachedResolution(true).withGigahorse(false),
  scalastyleFailOnError := true,
  scalacOptions += "-Yrangepos",
  scalacOptions in (Compile, doc) := (scalacOptions in (Compile, doc)).value filterNot (_ == "-Xfatal-warnings"),
  // Use this task to practice shame driven development (SDD)
  validate := {
    lint.value
    validate.value
  },
  lint := {
    forSideEffect((Compile / scalafix).toTask(" --test").value)
    forSideEffect((Test / scalafix).toTask(" --test").value)
    forSideEffect((Compile / scalafmtCheck).value)
    forSideEffect((Test / scalafmtCheck).value)
    forSideEffect((Compile / scalafmtSbtCheck).value)
    forSideEffect((Compile / scalastyle).toTask("").value)
  },
  format := {
    (Compile / scalafmtSbt).value
    scalafmtAll.value
  },
  // Wart Remover settings; please read and understand
  // http://www.wartremover.org/doc/warts.html before making any changes here.
  wartremoverErrors in (Compile, compile) ++= Warts.allBut(
    // These are covered by scalac
    Wart.Any,
    Wart.AnyVal,
    // These are covered by scalafix
    Wart.Null,
    Wart.Var,
    Wart.FinalVal,
    Wart.AsInstanceOf,
    // Inferring Nothing isn't really a problem. -ceedubs
    Wart.Nothing,
    // Default arguments can be error-prone, but let's have a separate
    // discussion about whether we want to disable them.
    Wart.DefaultArguments,
    // This is a little dodgy, but we are relying on it a lot, so let's have a
    // discussion on it.
    Wart.Option2Iterable,
    // Non-unit statements can be indications of bugs, but sometimes Spark
    // forces us into them, and they are gross to work around.
    Wart.NonUnitStatements,
    // currently doesn't play nice with simulacrum
    Wart.PublicInference
  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),
  addCompilerPlugin(scalafixSemanticdb)
)

///////////////////////////////////////////////////////////
// Project definitions

lazy val root = (project in file("."))
  .settings(name := "animal-house", description := "Code usw for creating a request classifier")
  .enablePlugins(BasicSmrtsPlugin, SmrtPublishingPlugin, SmrtReleasePlugin, SmrtDatabricksPlugin)
  .settings(commonSettings)
  .settings(coverageOutputTeamCity := true)
  .settings(libraryDependencies ++= Seq(
    Dependencies.scalatest % Test,
    Dependencies.scalacheck % Test,
    Dependencies.smrtTestkit.core % Test,
    Dependencies.smrtTestkit.spark % Test,
    Dependencies.spark.core,
    Dependencies.spark.sql,
    Dependencies.spark.ml,
    Dependencies.alchemy.smrtTools
  ))
