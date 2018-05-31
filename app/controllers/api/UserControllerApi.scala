package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import daos.UserDao
import models.User
import play.api.mvc.{AbstractController, ControllerComponents}
import services.UserService

import scala.concurrent.{ExecutionContext, Future}
import scalaz.EitherT._
import scalaz.Scalaz._
import scalaz._

@Singleton
class UserControllerApi @javax.inject.Inject()(cc: ControllerComponents, usersDao: UserDao, userService: UserService)
                                             (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._

  def allUsers = Action.async {
    val fUsers = usersDao.all()
    fUsers.map(users => CustomResponse.apply(Right(users)))
  }

  def saveUser = Action.async(parse.json) { implicit rs =>
    CustomResponse.asyncResultz {
      val result: EitherT[Future, CustomResponse.ApiError, String] =
        for {
          jsonUser <- EitherT(CustomResponse.jsonToResponseAsync(rs.body.validate[User]))
          msg <- EitherT(usersDao.insert(jsonUser))
        } yield {
          msg
        }
      result.run
    }
  }

  def updateUser = Action.async(parse.json) { implicit rs =>
    CustomResponse.asyncResultz {
      val result = for {
        jsonUser <- EitherT(CustomResponse.jsonToResponseAsync(rs.body.validate[User]))
        _ <- EitherT(usersDao.update(jsonUser))
      } yield {
        "Usuario guardado"
      }
      result.run
    }
  }

  def getUser(userId: Long) = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      val result = for {
        user <- EitherT(userService.getUser(userId))
      } yield {
        user
      }
      val res = result.run
      res
    }
  }

  def deleteUser(userId: Long) = Action.async { implicit rs =>
    CustomResponse.asyncResultz{
      val result = for{
        user <- EitherT(userService.getUser(userId))
        _ <- EitherT(userService.deleteUser(user))
      } yield {
        "Usuario eliminado"
      }
      result.run
    }
  }
}
