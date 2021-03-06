name          := "Kalaha"
organization  := "de.htwg.se"
version       := "1.0.0"
scalaVersion  := "2.12.7"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.jcenterRepo

libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "com.google.inject" % "guice" % "3.0"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

//libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"

//libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.8"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.19"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.1"
)