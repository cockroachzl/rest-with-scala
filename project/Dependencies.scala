import sbt._

object DependenciesCommon {

  val backendDeps = Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "ch.qos.logback" % "logback-classic" % "1.1.2")
  val resolvers = Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/")
}

object DependenciesChapter1 {
  lazy val http4sVersion = "0.7.0"

  val backendDeps = Seq("org.http4s" %% "http4s-dsl" % http4sVersion,
        "org.http4s" %% "http4s-blazeserver"  % http4sVersion)

  val resolvers =
    Seq("Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases")

}

object DependenciesChapter2 {
  lazy val finchVersion = "0.7.0"
  val backendDeps = Seq(
    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-argonaut" % finchVersion
  )


  val resolvers =
    Seq("Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")


}