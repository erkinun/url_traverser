name := """url-traverser"""

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

//fork in run := true

connectInput in run := true

val downloadLicense = taskKey[File]("downloads a license")
val licenseUrl = settingKey[String]("license url")
//licenseUrl := "http://opensource.apple.com/source/tcl/tcl-20/tcl_ext/snack/snack/BSD.txt?txt"
licenseUrl := "http://www.gnu.org/licenses/gpl-3.0.txt"

downloadLicense := {
  //val url =
  val bd = baseDirectory.value
  val file = bd / "LICENSE.txt"

  import com.ning.http.client.AsyncHttpClientConfig.Builder
  import play.api.libs.ws.ning.NingWSClient

  val client = new NingWSClient(new Builder().build()).url(licenseUrl.value)

  val result = client.get()

  import concurrent.Await
  import concurrent.duration.Duration.Inf

  val licenseText = Await.result(result, Inf).body
  IO.write(file, licenseText)
  file
}