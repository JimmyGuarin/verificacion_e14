package services

import javax.inject.Singleton
import core.CustomResponse
import daos.{CandidatoDao, DetalleReporteSospechosoDao, E14Dao, ReporteE14Dao}
import models._
import play.api.http.Status

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random
import core.CustomResponse.{ApiResponsez, _}
import core.util.{DetalleReporteJson, ReporteE14Json}
import scalaz._
import Scalaz._

@Singleton
class ReportesService @javax.inject.Inject()(e14Dao: E14Dao, reporteE14Dao: ReporteE14Dao, detalleReporteDao: DetalleReporteSospechosoDao)(
  implicit executionContext: ExecutionContext){

  val random = new Random()

  def getRandomE14(usuario: Usuario): Future[ApiResponsez[E14]] = {

    def nextRandomE14(maxId: Int, totalE14: Int): Future[Option[E14]] = Future.apply {
      var nextRandomE14: Option[E14] = None
      val totalPorUsuario = Await.result(reporteE14Dao.totalReportesPorUsuario(usuario.id.get), Duration.Inf) == totalE14
      var stop = totalPorUsuario

      while(!stop){
        val randomId = random.nextInt(maxId)
        val next = reporteE14Dao.getReporte(usuario.id.get, randomId)
        val res = Await.result(next, Duration.Inf)
        if(res.isEmpty){
          val maybeE14 = Await.result(e14Dao.getById(randomId), Duration.Inf)
          if(maybeE14.isDefined) {
            nextRandomE14 = maybeE14
            stop = true
          }
        }
      }
      nextRandomE14
    }

    for{
      maxId <- e14Dao.getMaxId()
      totalE14 <- e14Dao.totalRegistros()
      nextRandom <- nextRandomE14(maxId, totalE14)
    } yield {
      nextRandom.toRightDisjunction(ApiError(Status.BAD_REQUEST, "E14 no disponible", "E14 no disponible"))
    }
  }

  def guardarReporte(usuario: Usuario, reporteJson: ReporteE14Json): Future[CustomResponse.ApiResponsez[String]] = {
    val reporteE14 = ReporteE14(reporteJson.e14Id, usuario.id.get, reporteJson.valido)

    for{
      reporteGuardado <- reporteE14Dao.guardarReporte(reporteE14)
      mensaje <- guardarDetalles(reporteGuardado, reporteJson.detalles)
    } yield {
      mensaje
    }
  }

  private def guardarDetalles(reporte: ReporteE14, detallesReporteJson: Seq[DetalleReporteJson]): Future[CustomResponse.ApiResponsez[String]] = {
    val detallesReporte = detallesReporteJson.map{ detalle =>
      DetalleReporteSospechoso(reporte.id.get, detalle.candidatoId, detalle.votosSospechosos)
    }
    val seqFuture = detallesReporte.map { detalle =>
      detalleReporteDao.guardarDetalle(detalle)
    }

    Future.sequence(seqFuture).map { seqSaves =>
      val zero: CustomResponse.ApiResponsez[String] = "Nothing saved".right
      seqSaves.foldLeft(zero){ case (nextResponse, _) =>
          nextResponse
      }
    }
//    Future.sequence(seqFuture)
  }

}
