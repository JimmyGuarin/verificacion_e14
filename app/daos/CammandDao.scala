package daos

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject

import models.Command
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class CammandDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  private val Commands = TableQuery[CommandsTable]

  def all(): Future[Seq[Command]] = db.run(Commands.result)

  def insert(command: Command): Future[Unit] = db.run(Commands += command).map { _ => () }


  private class CommandsTable(tag: Tag) extends Table[Command](tag, "Command") {

    def id = column[Long] ("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def value = column[String]("value")
    def description = column[String]("description")

    def userId = column[Long] ("User_id")

//    def users = foreignKey("fk_Command_User", userId, users)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)


    def categoryId = column[Long] ("Category_id")


    def * = (title, value,description, userId, categoryId, id.?) <> (Command.tupled, Command.unapply)
  }
}
