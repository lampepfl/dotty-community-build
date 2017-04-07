organization in ThisBuild := "ch.epfl.lamp"
scalaVersion in ThisBuild := "2.12.1"

lazy val community = project
  .in(file("dotty-community-build"))
  .settings(
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test,
    testOptions in Test += Tests.Argument(TestFrameworks.JUnit)
  )
