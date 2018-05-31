package daos

import javax.inject.Inject

import core.CustomResponse.ApiResponsez
import models.{E14, ReporteE14, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

import scalaz._
import Scalaz._

class ReporteE14Dao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val reportesE14Table = TableQuery[ReportesE14Table]

  def all(): Future[Seq[ReporteE14]] = db.run(reportesE14Table.result)

  def getReporte(usuarioId: Int, e14Id: Int): Future[Option[ReporteE14]] = {
    val result = reportesE14Table.filter(r => r.usuarioId === usuarioId && r.e14Id === e14Id).result
    db.run(result).map(_.headOption)
  }

  def guardarReporte(reporteE14: ReporteE14): Future[ApiResponsez[String]] = {
    db.run(reportesE14Table += reporteE14).map { _ => "Guardado correctamente".right }
  }

  def totalReportesPorUsuario(usuarioId: Int): Future[Int] = {
    val result = reportesE14Table.filter(r => r.usuarioId === usuarioId).length.result
    db.run(result)
  }

  private class ReportesE14Table(tag: Tag) extends Table[ReporteE14](tag, "reporte_e14") {
    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def e14Id = column[Int]("e14_id")
    def usuarioId = column[Int]("usuario_id")
    def valido = column[Boolean]("valido")

    def * = (e14Id, usuarioId, valido, id.?) <> (ReporteE14.tupled, ReporteE14.unapply)
  }
}
