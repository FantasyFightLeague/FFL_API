import sbt._
import Keys._

object MacroBuild extends Build {
  lazy val main = Project("ffl_api", file(".")) dependsOn macroSub
  lazy val macroSub = Project("macro", file("macro")) settings(
    libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
    )
}