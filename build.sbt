val scala3Version = "3.5.1"//"3.4.2"
val zioVersion = "2.0.15"
val tapirVersion = "1.11.6"//"1.11.1"
val quillVersion = "4.8.5"
val postgresVersion = "42.5.0"
val zioConfigVersion  = "3.0.7" // old version
val sttpVersion = "3.8.8" // old version
val javaMailVersion = "2.0.1"


lazy val root = project
  .in(file("."))
  .settings(
    name := "patient-server",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-Xmax-inlines", "50", "-Wunused:all"), // Maximal number of successive inlines (32) exceeded, -Xmax-inlines to change the limit.
    libraryDependencies += "dev.zio" %% "zio" % zioVersion,
    libraryDependencies += "dev.zio" %% "zio-json" % "0.6.0",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.1",//"3.0.0-RC2",

    // libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
    // https://mvnrepository.com/artifact/com.softwaremill.sttp.tapir/tapir-json-zio
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % tapirVersion,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % tapirVersion,
    
    libraryDependencies += "io.getquill" %% "quill-jdbc-zio" % quillVersion,
    libraryDependencies += "io.getquill" %% "quill-jdbc" % quillVersion,

    libraryDependencies += "dev.zio" %% "zio-config"  % zioConfigVersion,
    libraryDependencies += "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
    libraryDependencies += "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,

    libraryDependencies += "com.axiom" %% "dataimportcsv3s" % "0.0.1-SNAPSHOT",

    libraryDependencies += "org.postgresql" % "postgresql" % postgresVersion,
    libraryDependencies += "com.auth0" % "java-jwt" % "4.4.0",

    libraryDependencies += "com.sun.mail" % "jakarta.mail" % javaMailVersion,

    libraryDependencies += "com.softwaremill.sttp.client3" %% "zio" % sttpVersion % Test,
    libraryDependencies += "io.github.scottweaver" %% "zio-2-0-testcontainers-postgresql" % "0.9.0" % Test,

    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
    libraryDependencies += "dev.zio" %% "zio-test-sbt" % "2.1.6" % Test,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )