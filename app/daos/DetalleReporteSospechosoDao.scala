package daos

import javax.inject.Inject

import models.{DetalleReporteSospechoso, ReporteE14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class DetalleReporteSospechosoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val detallesReporteSospechosoTable = TableQuery[DetallesReporteSospechosoTable]

  def all(): Future[Seq[DetalleReporteSospechoso]] = db.run(detallesReporteSospechosoTable .result)

  private class DetallesReporteSospechosoTable(tag: Tag) extends Table[DetalleReporteSospechoso](tag, "detalle_reporte_sospechoso") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def reporteId = column[Int]("reporte_id")
    def candidatoId = column[Int]("candidato_id")
    def votosSospechosos = column[Int]("votos_sospechosos")

    def * = (reporteId, candidatoId, votosSospechosos, id.?) <> (DetalleReporteSospechoso.tupled, DetalleReporteSospechoso.unapply)
  }
}
