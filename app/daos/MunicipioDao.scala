package daos

import javax.inject.Inject

import models.{E14, Municipio}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class MunicipioDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val municipiosTable = TableQuery[MunicipiosTable]

  def all(): Future[Seq[Municipio]] = db.run(municipiosTable.result)


  private class MunicipiosTable(tag: Tag) extends Table[Municipio](tag, "municipio") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def idDepto = column[Int]("id_depto")

    def codigo = column[String]("codigo")
    def nombre = column[String]("nombre")

    def * = (idDepto, nombre, codigo, id.?) <> (Municipio.tupled, Municipio.unapply)
  }
}
