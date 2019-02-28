package daos

import javax.inject.Inject
import core.CustomResponse.ApiResponsez
import models.{DetalleReporteSospechoso, E14, ReporteE14}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._

class DetalleReporteSospechosoDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private[daos] val detallesReporteSospechosoTable = TableQuery[DetallesReporteSospechosoTable]

  def all(): Future[Seq[DetalleReporteSospechoso]] = db.run(detallesReporteSospechosoTable .result)

  def guardarDetalle(detalleReporte: DetalleReporteSospechoso): Future[ApiResponsez[String]] = db.run(detallesReporteSospechosoTable += detalleReporte).map { _ => "Guardado correctamente".right }

  def getById(id: Int):  Future[Option[DetalleReporteSospechoso]] = {
    val result = detallesReporteSospechosoTable.filter(_.id === id).result
    db.run(result).map(_.headOption)
  }

  def actualizar(detalleReporte: DetalleReporteSospechoso): Future[ApiResponsez[Int]] =
    db.run(detallesReporteSospechosoTable.filter(_.id === detalleReporte.id.get).update(detalleReporte)).map { _.right}

  def getAllSource() = {
    akka.stream.scaladsl.Source.fromPublisher(db.stream(detallesReporteSospechosoTable.result))
  }

  private[daos] class DetallesReporteSospechosoTable(tag: Tag) extends Table[DetalleReporteSospechoso](tag, "detalle_reporte_sospechoso") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def reporteId = column[Int]("reporte_id")
    def candidatoId = column[Int]("candidato_id")
    def votosSospechosos = column[Int]("votos_sospechosos")

    def data = column[Array[Byte]]("data")

    def * = (reporteId, candidatoId, votosSospechosos, data.?, id.?) <> (DetalleReporteSospechoso.tupled, DetalleReporteSospechoso.unapply)
  }
}
