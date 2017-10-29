lazy val dottyVersion = settingKey[String]("The version of Dotty to use.")

inThisBuild(List(
  organization := "ch.epfl.lamp",
  scalaVersion := "2.12.4"
))

dottyVersion := sys.env.getOrElse("DOTTY_VERSION", dottyLatestNightlyBuild.get)

lazy val `dotty-community-build` = project
  .in(file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),

    // BuildInfo plugin settings
    buildInfoKeys += (dottyVersion: BuildInfoKey),
    buildInfoPackage := "dotty.communitybuild"
  )
