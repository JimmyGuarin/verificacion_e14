package daos

import javax.inject.Inject

import models.{E14, Usuario}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UsuarioDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val usuariosTable = TableQuery[UsuariosTable]
  private val insertQuery = usuariosTable returning usuariosTable.map(_.id) into ((usuario, id) => usuario.copy(id = Some(id)))

  def all(): Future[Seq[Usuario]] = db.run(usuariosTable.result)

  def insertar(usuario: Usuario): Future[Usuario] = {
    db.run(insertQuery += usuario)
  }

  def usuarioPorId(id: Int): Future[Option[Usuario]] = {
    db.run(usuariosTable
      .filter(_.id === id)
      .result
      .headOption
    )
  }

  def usuarioPorIdGoogle(googleId: String): Future[Option[Usuario]]  = {
    db.run(
      usuariosTable.filter(_.googleId === googleId).result
    ).map(
      _.headOption
    )
  }

  private class UsuariosTable(tag: Tag) extends Table[Usuario](tag, "usuario") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def googleId = column[String]("google_id")
    def email = column[String]("email")
    def name = column[String]("name")


    def * = (googleId, email, name, id.?) <> (Usuario.tupled, Usuario.unapply)
  }
}
