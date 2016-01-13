name := """url traverser"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.2.0"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "0.1.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"

fork in run := true

connectInput in run := true
