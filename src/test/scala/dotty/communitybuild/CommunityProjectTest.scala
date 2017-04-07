package dotty.communitybuild

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Assert._
import org.junit.Test

case class CommunityProject(
    name: String,
    sbtCommands: List[String] = List("clean", "compile")
) {
  def sbtCommand: String = sbtCommands.mkString("; ", " ; ", "")
  def workingDirectory: Path = Paths.get(name)
}

abstract class CommunityProjectTest(project: CommunityProject) {
  @Test def compilesWithDotty(): Unit = {
    println(s"Starting....")
    def exec(command: String*): Unit = {
      println(s"Running command: ${command.mkString(" ")}")

      import scala.collection.JavaConverters._
      val builder = new ProcessBuilder(command.asJava)
        .directory(project.workingDirectory.toFile)
        .inheritIO()
      builder
        .environment()
        .put("COMPILERVERSION", "0.1.1-SNAPSHOT")

      val process = builder.start()
      val exit = process.waitFor()
      assertEquals(exit, 0)
    }
    println(project.workingDirectory)
    exec("git", "clean", "-xfd")
    exec("git", "checkout", "dotty")
    exec("sbt", project.sbtCommand)
    println(s"DONE!")
  }
}

class Scalatest extends CommunityProjectTest(CommunityProject("scalatest"))
