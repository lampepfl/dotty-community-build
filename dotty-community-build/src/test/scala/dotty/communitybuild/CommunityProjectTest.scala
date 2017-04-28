package dotty.communitybuild

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.junit.Assert._
import org.junit.Test

case class CommunityProject(name: String) {
  def workingDirectory: Path =
    Paths.get(sys.props("user.dir")).getParent.resolve(name)
}

abstract class CommunityProjectTest(project: CommunityProject) {
  private def log(msg: String) = {
    val banner = "*" * (msg.length + 5)
    println(banner)
    println("===> " + msg)
    println(banner)
  }
  log(s"Using dotty version ${BuildInfo.dottyVersion}")
  private val SbtCommand = ".*sbt ([^ ]+) .*".r
  @Test def compilesWithDotty(): Unit = {
    log(s"Starting....")
    // By convention, run the first command passed to sbt in .travis.yml
    val sbtCommand = new String(Files.readAllBytes(
      project.workingDirectory.resolve(".travis.yml"))).lines
      .sliding(2)
      .collectFirst {
        case "script:" :: SbtCommand(cmd) :: Nil => cmd
      }
      .getOrElse {
        throw new IllegalStateException(
          s"Unable to infer sbt command to test project $project")
      }

    def exec(binary: String, arguments: String*): Unit = {
      val command = binary +: arguments
      log(s"Running command: ${command.mkString(" ")}")

      import scala.collection.JavaConverters._
      val builder = new ProcessBuilder(command.asJava)
        .directory(project.workingDirectory.toFile)
        .inheritIO()

      val process = builder.start()
      val exit = process.waitFor()
      assertEquals(exit, 0)
    }
    exec("git", "clean", "-xfd")
    exec("git", "checkout", "dotty")
    exec("git", "pull", "origin", "dotty")
    exec("sbt", s"++${BuildInfo.dottyVersion}", sbtCommand)
    log(s"DONE!")
  }
}

class Scalatest extends CommunityProjectTest(CommunityProject("scalatest"))

class Squants extends CommunityProjectTest(CommunityProject("squants"))

class Algebra extends CommunityProjectTest(CommunityProject("algebra"))
