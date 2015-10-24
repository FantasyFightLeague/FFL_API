name := "FFL_API"

version := "1.0"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6")

scalacOptions += "-Ymacro-debug-lite"

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.2"
  Seq(
    "ch.qos.logback" % "logback-core" % "1.1.3",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.slf4j" % "slf4j-api" % "1.7.12",
    "org.mongodb" %% "casbah" % "2.8.1",
    "com.novus" %% "salat" % "1.9.9",
    "org.scalaz" %% "scalaz-core" % "7.0.6",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.json4s" %% "json4s-ext" % "3.2.11",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
}

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
    