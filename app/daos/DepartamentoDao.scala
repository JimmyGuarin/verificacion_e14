package daos

import javax.inject.Inject

import models.{Departamento, E14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class DepartamentoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val DepartamentosTable = TableQuery[DepartamentosTable]

  def all(): Future[Seq[Departamento]] = db.run(DepartamentosTable.result)


  private class DepartamentosTable(tag: Tag) extends Table[Departamento](tag, "departamento") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def codigo = column[String]("codigo")
    def nombre = column[String]("nombre")

    def * = (nombre, codigo, id.?) <> (Departamento.tupled, Departamento.unapply)
  }
}
