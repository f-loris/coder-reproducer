import sbt._
import Keys._

val scioVersion = "0.11.4"
val beamVersion = "2.35.0"

lazy val commonSettings = Def.settings(
    organization := "com.aeroficial",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.12.15",
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    scalacOptions ++= Seq("-target:jvm-1.8",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:higherKinds",
        "-Xmacro-settings:show-coder-fallback=true"
        //    "-Ymacro-annotations",

    ),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
)

lazy val root: Project = project
        .in(file("."))
        .settings(commonSettings)

        .settings(
            name := "coder-pipeline",
            description := "coder-pipeline",
            publish / skip := true,
            run / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
            run / fork := true,

            libraryDependencies ++= Seq(
                "com.spotify" %% "scio-core" % scioVersion,
                "com.spotify" %% "scio-test" % scioVersion % Test,
                "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
                "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % beamVersion,

                "org.slf4j" % "slf4j-simple" % "1.7.36"
            )
        )
        .enablePlugins(JavaAppPackaging)

lazy val repl: Project = project
        .in(file(".repl"))
        .settings(commonSettings)
        .settings(
            name := "repl",
            description := "Scio REPL for $name$",
            libraryDependencies ++= Seq(
                "com.spotify" %% "scio-repl" % scioVersion
            ),
            Compile / mainClass := Some("com.spotify.scio.repl.ScioShell"),
            publish / skip := true
        )
        .dependsOn(root)

