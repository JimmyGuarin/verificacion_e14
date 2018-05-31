package daos

import javax.inject.Inject

import models.{E14, Usuario}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UsuarioDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val UsuariosTable = TableQuery[UsuariosTable]

  def all(): Future[Seq[Usuario]] = db.run(UsuariosTable.result)


  private class UsuariosTable(tag: Tag) extends Table[Usuario](tag, "usuarios") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def twitterData = column[String]("twitterData")


    def * = (twitterData, id.?) <> (Usuario.tupled, Usuario.unapply)
  }
}
