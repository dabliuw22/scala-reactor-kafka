name := "scala-reactor-kafka"

version := "0.1"

scalaVersion := "2.13.1"

val akkaParent = "com.typesafe.akka"
val akkaVersion = "2.5.23"
val scalaLoggingParent = "com.typesafe.scala-logging"
val scalaLoggingVersion = "3.9.2"
val logbackParent = "ch.qos.logback"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  akkaParent %% "akka-actor" % akkaVersion,
  "io.projectreactor" % "reactor-scala-extensions_2.13.0-M3" % "0.4.7",
  "io.projectreactor.kafka" % "reactor-kafka" % "1.2.0.RELEASE",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.0",
  //"com.fasterxml.jackson.core" % "jackson-databind" % "2.10.0",
  scalaLoggingParent %% "scala-logging" % scalaLoggingVersion,
  logbackParent % "logback-classic" % logbackVersion,
)