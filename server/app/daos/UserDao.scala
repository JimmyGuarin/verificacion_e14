package daos
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import core.CustomResponseTypes
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class UserDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(
  implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import core.CustomResponse._

  import profile.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[ApiResponse[String]] = db.run(Users += user).map { _ => Right("Guardado correctamente") }


  private class UsersTable(tag: Tag) extends Table[User](tag, "User") {

    def id = column[Long] ("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String] ("email")
    def password = column[String] ("password")


    def * = (id, firstName, lastName, email, password) <> (User.tupled, User.unapply)
  }
}
