scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6")

libraryDependencies ++= {
  Seq(
    "org.mongodb" %% "casbah" % "2.8.1",
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
}

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)

