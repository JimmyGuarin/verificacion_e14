package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import daos.UserDao
import models.User
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scalaz.EitherT._
import scalaz.Scalaz._
import scalaz._

@Singleton
class UserControllerApi@javax.inject.Inject()(cc: ControllerComponents, usersDao: UserDao)
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
          jsonUser <- fromEither(CustomResponse.jsonToResponseAsync(rs.body.validate[User]))
          msg <- fromEither(usersDao.insert(jsonUser))
        } yield {
          msg
        }
      result.run
    }
  }
}
