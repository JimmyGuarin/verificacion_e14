package daos
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.http.Status
import slick.jdbc.JdbcProfile

import scalaz._
import Scalaz._


class UserDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(
  implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import core.CustomResponse._

  import profile.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[ApiResponsez[String]] = db.run(Users += user).map { _ => "Guardado correctamente".right }

  def update(user: User): Future[ApiResponsez[Int]] = db.run(Users.update(user)).map { _.right}

  def delete(user: User): Future[Int] = db.run(Users.filter(_.id === user.id.get).delete)

  def getUser(id: Long): Future[Option[User]] = {
    val userQuery = Users.filter(_.id === id).result.headOption
    db.run(userQuery)
  }

  private class UsersTable(tag: Tag) extends Table[User](tag, "User") {

    def id = column[Long] ("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String] ("email")
    def password = column[String] ("password")


    def * = (firstName, lastName, email, password, id.?) <> (User.tupled, User.unapply)
  }
}
