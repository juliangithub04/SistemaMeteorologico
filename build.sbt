ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "SistemaMeteorologico",

    // Añade dependencias aquí
    libraryDependencies ++= Seq(
      // Aquí puedes agregar más dependencias si las necesitas, por ejemplo:
      // "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20" // Para usar Akka en caso de necesitar concurrencia avanzada
    )
  )
