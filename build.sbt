val scala3Version = "3.4.2"
val zioVersion = "2.0.15"
val tapirVersion = "1.11.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "patient-server",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,

    libraryDependencies += "dev.zio" %% "zio" % zioVersion,
    libraryDependencies += "dev.zio" %% "zio-json" % "0.6.0",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC2",

    // libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
    // https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-json-zio
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,

    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,


    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )
