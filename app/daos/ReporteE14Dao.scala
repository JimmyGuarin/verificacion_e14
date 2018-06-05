package daos

import javax.inject.Inject

import core.CustomResponse.ApiResponsez
import models.{DetalleReporteSospechoso, E14, ReporteE14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._

class ReporteE14Dao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, detalleDao: DetalleReporteSospechosoDao)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private[daos] val reportesE14Table = TableQuery[ReportesE14Table]

  def all(): Future[Seq[ReporteE14]] = db.run(reportesE14Table.result)

  def getReporte(usuarioId: Int, e14Id: Int): Future[Option[ReporteE14]] = {
    val result = reportesE14Table.filter(r => r.usuarioId === usuarioId && r.e14Id === e14Id).result
    db.run(result).map(_.headOption)
  }

  def reportesInvalidosConDetalles: Future[scala.Seq[(ReporteE14, DetalleReporteSospechoso)]] = {
    //TODO check that join works
    val joinResult = for {
      reporteInvalido <- reportesE14Table.filter(_.valido === true)
//      (reporte, detalle) <- novalidos join detalleDao.detallesReporteSospechosoTable on (_.id === _.reporteId)
      detalle <- detalleDao.detallesReporteSospechosoTable if reporteInvalido.id === detalle.reporteId
    } yield {
      (reporteInvalido, detalle)
    }
    db.run(joinResult.result)
  }

  private val insertQuery = reportesE14Table returning reportesE14Table.map(_.id) into ((item, id) => item.copy(id = Some(id)))

  def guardarReporte(reporteE14: ReporteE14): Future[ReporteE14] = {
    db.run(insertQuery += reporteE14)
  }

  def totalReportesPorUsuario(usuarioId: Int): Future[Int] = {
    val result = reportesE14Table.filter(r => r.usuarioId === usuarioId).length.result
    db.run(result)
  }

  private[daos] class ReportesE14Table(tag: Tag) extends Table[ReporteE14](tag, "reporte_e14") {
    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def e14Id = column[Int]("e14_id")
    def usuarioId = column[Int]("usuario_id")
    def valido = column[Boolean]("valido")

    def * = (e14Id, usuarioId, valido, id.?) <> (ReporteE14.tupled, ReporteE14.unapply)
  }
}
