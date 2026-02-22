ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.18"
// 1. Defina a versão como uma String
val slickV = "3.5.1"

lazy val root = (project in file("."))
  .settings(
    name := "scala-back-end"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
  "com.typesafe.akka" %% "akka-stream" % "2.8.5",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.11.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",
  // 2. Use a variável slickV aqui (sem aspas no nome da variável)
  "com.typesafe.slick" %% "slick"          % slickV,
  "com.typesafe.slick" %% "slick-hikaricp" % slickV,

  // Driver do PostgreSQL
  "org.postgresql"      % "postgresql"     % "42.7.2",

  // Mantendo suas outras dependências (Akka, Mongo, etc)
  "org.mongodb.scala"  %% "mongo-scala-driver" % "4.11.0"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.6"