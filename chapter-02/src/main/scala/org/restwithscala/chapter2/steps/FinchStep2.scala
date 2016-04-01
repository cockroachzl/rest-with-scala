/*

package org.restwithscala.chapter2.steps

import com.twitter.finagle.httpx.Request
import com.twitter.finagle.{Service, Httpx}
import com.twitter.util.Future
import io.finch.request._
import io.finch.response.{NotFound, Ok}
import io.finch.route._
import io.finch.{Endpoint => _, _}
import org.restwithscala.common.model.{Status, Note, Person, Task}
import org.restwithscala.common.service.TaskService
import org.restwithscala.chapter2.scalaToTwitterFuture

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


/**
 * In the second step we'll manually parse the incoming json using request readers
 * in a service and parse it to case class. We don't use any JSON yet, we just simply
 * use a number of request parameters.
 *
 * We use implicit conversions from scala future to twitter future to work with the
 * TaskService.
 */
object FinchStep2 extends App {

  val matchTask: Matcher = "tasks"
  val matchTaskId = matchTask / long

  // handle a single post using a RequestReader
  val taskCreateAPI =
        Get / matchTask /> GetAllTasks() :+:
       Post / matchTask /> CreateNewTask() :+:
     Delete / matchTaskId /> DeleteTask :+:
        Get / matchTaskId /> GetTask :+:
        Put / matchTaskId /> UpdateTask

  val taskAPI =
        Get / matchTaskId /> ( id => s"Get a single task with id: $id" ) |
        Get / matchTaskId / "owner" /> ( id => s"Get the owner of task with $id" ) |
       Post / matchTaskId / "status" /> ( id => s"Set the status of taskk with $id to " )
        Get / matchTaskId / "status" /> ( id => s"Get the status of an existing task with $id" ) |
        Get / matchTaskId / "notes" /> ( id => s"Get all notes for task with id: $id" ) |
       Post / matchTaskId /> ( id => s"Create a new note for task with id $id to " ) |
        Put / matchTaskId / "notes" / long /> ( (taskId, noteId) => s"Update an existing note with noteid:  $noteId with " ) |
        Get / matchTaskId / "notes" / long /> ( (taskId, noteId) => s"Get note with id:$noteId for task with id: $taskId" ) |
     Delete / matchTaskId / "notes" / long /> ( (taskId, noteId) => s"Delete an existing note with $noteId" )

  // simple server, which combines the two routes and creates a service.
  val server = Httpx.serve(":8080", (taskAPI :+: taskCreateAPI).toService )

  println("Press <enter> to exit.")
  Console.in.read.toChar

  server.close()

  sealed trait BaseTask {

    /**
     * Reader converts the incoming request to a task. We combine
     * the various readers and return a task.
     */
    def getRequestToTaskReader(id: Long): RequestReader[Task] = {
      ( RequestReader.value(id) ::                       // generate a new id
        param("title") ::                                 // use req. param title as title
        body ::                                           // use the body as content
        RequestReader.value(Option.empty[Person]) ::       // default not assigned
        RequestReader.value(List.empty[Note]) ::          // no notes initially
        RequestReader.value(Status(""))                   // use status parameter as status
        ).as[Task]                                        // finally return a task
    }
  }

  /**
   * Create a new task. This class expects a httprequest, and returns a Future[String]
   */
  case class CreateNewTask() extends Service[Request, String] with BaseTask {

    /**
     * Called when a request is received. Tbis function should return:
     * An HttpResponse
     *
     * - A value of a type with an EncodeResponse instance
     * - A Future of HttpResponse
     * - A Future of a value of a type with an EncodeResponse instance
     * - A RequestReader that returns a value of a type with an EncodeResponse instance
     * - A Finagle service that returns an HttpResponse
     * - A Finagle service that returns a value of a type with an EncodeResponse instance
     */
    def apply(req: Request): Future[String] = {
      val p = for {
        task <- getRequestToTaskReader(-1)(req)
        stored <- TaskService.insert(task)
      } yield stored

      p.map(_.toString)
    }
  }

  case class DeleteTask(id: Long) extends Service[Request, HttpResponse] {
    def apply(req: Request): Future[HttpResponse] = TaskService.delete(id).map {
      case Some(task) => Ok()
      case None => NotFound()
    }
  }

  case class GetAllTasks() extends Service[Request, HttpResponse] {
    def apply(req: Request): Future[HttpResponse] = {
      for {
        tasks <- TaskService.all
      } yield Ok(tasks.mkString(":"))
    }
  }

  case class GetTask(taskId: Long) extends Service[Request, HttpResponse] {
    def apply(req: Request): Future[HttpResponse] = {
      TaskService.select(taskId).map {
        case Some(task) => Ok(task.toString)
        case None => NotFound()
      }
    }
  }

  case class UpdateTask(taskId: Long) extends Service[Request, HttpResponse] with BaseTask {
    def apply(req: Request): Future[HttpResponse] =
      for {
        task <- getRequestToTaskReader(taskId)(req)
        stored <- TaskService.update(taskId, task)
      } yield stored match {
        case Some(task) => Ok(task.toString)
        case None => NotFound()
      }
  }
}


*/
