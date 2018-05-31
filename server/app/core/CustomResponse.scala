package core

import play.api.libs.json._
import play.api.mvc.Result

import scala.concurrent.{ExecutionContext, Future}
import scalaz.{Monad, \/}
import play.api.mvc.Results._
import play.api.http.Status._

trait CustomResponseTypes {

  import scala.concurrent.ExecutionContext.Implicits.global

  case class ApiError(code: Int, userMessage: String, message: String)

  type ApiResponse[A] = Either[ApiError, A]

  type ApiResponsez[A] = \/[ApiError, A]

  implicit val futureMonad = new Monad[Future] {
    override def point[A](a: ⇒ A): Future[A] = Future.apply(a)

    override def bind[A, B](fa: Future[A])(f: A ⇒ Future[B]): Future[B] =
      fa.flatMap(f)
  }

}

object CustomResponse extends CustomResponseTypes{

  def apply[A](a: => ApiResponse[A])(implicit w: Writes[A]): Result = {
    a match {
      case Left(apiError) =>
        Status(apiError.code) {
          JsObject(Seq(
            "status" -> JsString("ERROR"),
            "status_code" -> JsNumber(apiError.code),
            "message" -> JsString(apiError.message),
            "userMessage" -> JsString(apiError.userMessage)
          ))
        }
      case Right(res) =>
        Ok {
          JsObject(Seq(
            "status" -> JsString("SUCCESS"),
            "response" -> Json.toJson(res)
          ))
        }
    }
  }

  def asyncResult[A](futureResponse: => Future[ApiResponse[A]])(implicit w: Writes[A], ec: ExecutionContext): Future[Result] = {
    futureResponse.map { a =>
      a match {
        case Left(apiError) =>
          Status(apiError.code) {
            JsObject(Seq(
              "status" -> JsString("ERROR"),
              "status_code" -> JsNumber(apiError.code),
              "message" -> JsString(apiError.message),
              "userMessage" -> JsString(apiError.userMessage)
            ))
          }
        case Right(res) =>
          Ok {
            JsObject(Seq(
              "status" -> JsString("SUCCESS"),
              "response" -> Json.toJson(res)
            ))
          }
      }
    }
  }

  def asyncResultz[A](futureResponse: => Future[ApiResponsez[A]])(implicit w: Writes[A], ec: ExecutionContext): Future[Result] = {
    futureResponse.map { a =>
      a.fold(apiError => {
        Status(apiError.code) {
          JsObject(Seq(
            "status" -> JsString("ERROR"),
            "status_code" -> JsNumber(apiError.code),
            "message" -> JsString(apiError.message),
            "userMessage" -> JsString(apiError.userMessage)
          ))
        }
      }, res =>{
        Ok {
          JsObject(Seq(
            "status" -> JsString("SUCCESS"),
            "response" -> Json.toJson(res)
          ))
        }
      })
    }
  }

  def jsonToResponse[A](j: play.api.libs.json.JsResult[A]): ApiResponse[A] =
    j.fold(
      errors => {
        val f = errors.map {
          case (_, validation) => ApiError(BAD_REQUEST, "Invalid data",
            validation.map(_.message).mkString(","))
        }
        Left(f.head)
      },
      value =>
        Right(value))

  def jsonToResponseAsync[A](j: play.api.libs.json.JsResult[A]): Future[ApiResponse[A]] =
    j.fold(
      errors => {
        val f = errors.map {
          case (_, validation) => ApiError(BAD_REQUEST, "Invalid data",
            validation.map(_.message).mkString(","))
        }
        Future.successful(Left(f.head))
      },
      value =>
        Future.successful(Right(value)))
}
