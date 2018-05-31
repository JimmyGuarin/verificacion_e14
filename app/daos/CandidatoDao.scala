package daos

import javax.inject.Inject

import models.{Candidato, E14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CandidatoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val candidatosTable = TableQuery[CandidatosTable]

  def all(): Future[Seq[Candidato]] = db.run(candidatosTable.result)


  private class CandidatosTable(tag: Tag) extends Table[Candidato](tag, "candidato") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")


    def * = (nombre, id.?) <> (Candidato.tupled, Candidato.unapply)
  }
}
