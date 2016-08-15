name := "amaysim-demo"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  // FP
  "org.scalaz" %% "scalaz-core" % "7.2.5",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.5",

  // Http
  "org.http4s" %% "http4s-core" % "0.14.2a",
  "org.http4s" %% "http4s-dsl" % "0.14.2a",
  "org.http4s" %% "http4s-blaze-server" % "0.14.2a",
  "org.http4s" %% "http4s-blaze-client" % "0.14.2a",
  "io.argonaut" %% "argonaut" % "6.1a",
  "black.door" % "hate" % "v1r4t0",

  // CLI
  "com.github.scopt" %% "scopt" % "3.5.0",

  // Db
  "org.tpolecat" %% "doobie-core" % "0.3.0",
  "com.h2database" % "h2" % "1.4.192",

  // Logging
  "org.log4s" %% "log4s" % "1.3.0",
  "org.slf4j" % "slf4j-simple" % "1.7.21",

  // Test
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
