package services

import javax.inject.Singleton

import core.CustomResponse.ApiResponsez
import daos.UserDao
import models.User

import scala.concurrent.{ExecutionContext, Future}
import core.CustomResponse._
import play.api.http.Status

import scalaz._
import Scalaz._

@Singleton
class UserService @javax.inject.Inject() (usersDao: UserDao) (
  implicit executionContext: ExecutionContext){


  def getUser(id: Long): Future[ApiResponsez[User]] = {
    usersDao
      .getUser(id)
      .map(x => x.toRightDisjunction(ApiError(Status.NOT_FOUND, "Invalid user", "Invalid user")))
  }

  def deleteUser(user: User): Future[ApiResponsez[Unit]] = {
    usersDao.delete(user).map { _ => ().right}
  }
}
