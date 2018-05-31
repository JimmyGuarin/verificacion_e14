package daos

import javax.inject.Inject

import core.CustomResponse.ApiResponsez
import models.{DetalleReporteSospechoso, ReporteE14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

import scalaz._
import Scalaz._

class DetalleReporteSospechosoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val detallesReporteSospechosoTable = TableQuery[DetallesReporteSospechosoTable]

  def all(): Future[Seq[DetalleReporteSospechoso]] = db.run(detallesReporteSospechosoTable .result)

  def guardarDetalle(detalleReporte: DetalleReporteSospechoso): Future[ApiResponsez[String]] = db.run(detallesReporteSospechosoTable += detalleReporte).map { _ => "Guardado correctamente".right }


  private class DetallesReporteSospechosoTable(tag: Tag) extends Table[DetalleReporteSospechoso](tag, "detalle_reporte_sospechoso") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def reporteId = column[Int]("reporte_id")
    def candidatoId = column[Int]("candidato_id")
    def votosSospechosos = column[Int]("votos_sospechosos")

    def * = (reporteId, candidatoId, votosSospechosos, id.?) <> (DetalleReporteSospechoso.tupled, DetalleReporteSospechoso.unapply)
  }
}
