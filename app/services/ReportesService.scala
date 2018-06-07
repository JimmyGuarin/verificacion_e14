package services

import javax.inject.Singleton
import akka.actor.ActorSystem
import core.CustomResponse
import core.CustomResponse.{ApiResponsez, _}
import core.util._
import daos.{CandidatoDao, DetalleReporteSospechosoDao, E14Dao, ReporteE14Dao}
import models._
import play.api.cache.AsyncCacheApi
import play.api.http.Status
import play.api.libs.ws.WSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random
import scalaz.Scalaz._
import scalaz._

import scala.concurrent.duration._



case class CandVotoGroup(candId: Int, votos: Int)
case class DetallesCount(repetidosCount: Int, detalles:  Seq[(ReporteE14, DetalleReporteSospechoso)])
case class AgrupadoModa(candVotoGroup: CandVotoGroup, detallesCount: DetallesCount, todosGoups: Map[CandVotoGroup, DetallesCount])






@Singleton
class ReportesService @javax.inject.Inject()(e14Dao: E14Dao,
                                             wsClient: WSClient,
                                             candidatoDao: CandidatoDao,
                                             reporteE14Dao: ReporteE14Dao,
                                             detalleReporteDao: DetalleReporteSospechosoDao,
                                             cache: AsyncCacheApi,
                                             actorSystem: ActorSystem)(
                                              implicit executionContext: ExecutionContext) {

  import core.util.JsonFormats._

  val random = new Random()

  private val estadisticasCacheKey = "estadisticas"

  private val porcentajeVerificado = "porcentaje_verificado"


//  val estadisticasJob = actorSystem.scheduler.schedule(initialDelay = 0.seconds, interval = 1.minute) {
////    cache.remove(estadisticasCacheKey)
//    votosReportadosByCandidato.foreach(stats => cache.set(estadisticasCacheKey, stats))
//  }

  def getRandomE14(usuario: Usuario): Future[ApiResponsez[E14Encript]] = {

    def nextRandomE14(maxId: Int, totalE14: Int): Future[Option[E14]] = Future.apply {
      var nextRandomE14: Option[E14] = None
      val totalPorUsuario = Await.result(reporteE14Dao.totalReportesPorUsuario(usuario.id.get), Duration.Inf) == totalE14
      var stop = totalPorUsuario

      while (!stop) {
        val randomId = random.nextInt(maxId)
        val next = reporteE14Dao.getReporte(usuario.id.get, randomId)
        val res = Await.result(next, Duration.Inf)
        if (res.isEmpty) {
          val maybeE14 = Await.result(e14Dao.getById(randomId), Duration.Inf)
          if (maybeE14.isDefined) {
            nextRandomE14 = maybeE14
            stop = true
          }
        }
      }
      nextRandomE14
    }

    def encryptId(e14: E14): E14Encript = {
      val encryptId = Encryption.encrypt(usuario.googleId, e14.id.get.toString)
      E14Encript(e14.link, e14.departamento, e14.municipio, e14.zona, e14.puesto, encryptId)
    }

    def withStats(mE14: Option[E14]): Future[Option[(E14, Int)]] = {
      mE14 match {
        case None => Future.successful(None)
        case Some(e14) =>
          reporteE14Dao
            .totalReportes(e14.id.get)
            .map(reportes => Some((e14, reportes)))
      }
    }

    //Function Body
    for {
      maxId <- e14Dao.getMaxId()
      totalE14 <- e14Dao.totalRegistros()
      nextRandom <- nextRandomE14(maxId, totalE14)
      withStats <- withStats(nextRandom)
    } yield {
      withStats.map{ case (randomE14, reportes) =>
        encryptId(randomE14)
          .copy(reportes = reportes)
      }.toRightDisjunction(
        ApiError(Status.BAD_REQUEST, "E14 no disponible", "E14 no disponible")
      )
    }
  }

  def guardarReporte(usuario: Usuario, reporteJson: ReporteE14Json): Future[CustomResponse.ApiResponsez[String]] = {
    val e14Id = Encryption.decrypt(usuario.googleId, reporteJson.e14Id).toInt
    val reporteE14 = ReporteE14(e14Id, usuario.id.get, reporteJson.valido)

    for {
      reporteGuardado <- reporteE14Dao.guardarReporte(reporteE14)
      mensaje <- guardarDetalles(reporteGuardado, reporteJson.detalles)
    } yield {
      mensaje
    }
  }

  private def guardarDetalles(reporte: ReporteE14, detallesReporteJson: Seq[DetalleReporteJson]): Future[CustomResponse.ApiResponsez[String]] = {
    val detallesReporte = detallesReporteJson.map { detalle =>
      DetalleReporteSospechoso(reporte.id.get, detalle.candidatoId, detalle.votosSospechosos)
    }
    val seqFuture = detallesReporte.map { detalle =>
      detalleReporteDao.guardarDetalle(detalle)
    }

    Future.sequence(seqFuture).map { seqSaves =>
      val zero: CustomResponse.ApiResponsez[String] = "Nothing saved".right
      seqSaves.foldLeft(zero) { case (nextResponse, _) =>
        nextResponse
      }
    }
    //    Future.sequence(seqFuture)
  }

  def validarCaptcha(valorCaptcha: Option[String], e14Valido: Boolean): Future[CustomResponse.ApiResponsez[Unit]] = {
    if (e14Valido) {
      Future.apply(().right)
    } else {
      valorCaptcha.map(c => {
        val url = "https://www.google.com/recaptcha/api/siteverify"
        val response = wsClient.url(url)
          .post(Map("secret" -> Seq("6Ld9zlwUAAAAAEsWpx0O2CWbWGT9A9mjHZKoCwI3"), "response" -> Seq(c)))
        response.map { wsResponse =>
          val respuestaCaptcha = wsResponse.json.validate[RespuestaCaptcha].get
          if (respuestaCaptcha.success) {
            ().right
          } else {
            ApiError(400, "Error Validando Captcha", "Error Validando Captcha").left
          }
        }
      }).getOrElse(
        Future.apply(ApiError(400, "", "").left)
      )
    }
  }

  def estadisticas() = {
    val agrupadoPorE14 = reporteE14Dao.reportesInvalidosConDetalles.map { todosSeq =>
      todosSeq
        .groupBy(_._1.e4Id)
    }

    agrupadoPorE14.map { agrupados =>
      agrupados.map{ case (e14Id, reportes) =>
          val agrupadoCandVoto = reportes
            .groupBy{case (_, detalles) => (detalles.candidatoId, detalles.votosSospechoso)}
            .map{ case((candidatoId, votos), detalles) => (CandVotoGroup(candidatoId, votos), DetallesCount(detalles.size, detalles)) }
          val agrupadoCandVotoModa = agrupadoCandVoto.maxBy{case (key, value) => value.repetidosCount}
        (e14Id, AgrupadoModa(agrupadoCandVotoModa._1, agrupadoCandVotoModa._2, agrupadoCandVoto))
      }
    }

  }

  private def porcentajeE14Verificado: Future[Double] = {
    for{
      totalE14 <- e14Dao.totalRegistros()
      totalVerificados <- reporteE14Dao.totalVerificados
    } yield {
      totalVerificados.toDouble / totalE14.toDouble
    }
  }

  def porcentajeE14VerificadoFromCache: Future[Double] = {
    cache.getOrElseUpdate(porcentajeVerificado, 1.minute){
      porcentajeE14Verificado
    }
  }

  def estadisticasFromCache: Future[ResumenSumatoria] = {
    cache.getOrElseUpdate(estadisticasCacheKey, 5.minutes){
      votosReportadosByCandidato
    }
  }

  def agrupadosPorCandidatoYVoto: Future[Map[E14, Map[Candidato, DetallesGroupedByVotos]]]  = {
    val agrupadoPorE14 = reporteE14Dao.reportesInvalidosConDetalles.map(_.groupBy(_._1.e4Id))

    for {
      candidatos <- candidatoDao.all()
      e14ConReportesInvalidos <- e14Dao.e14ConReportesInvalidos
      agrupados <- agrupadoPorE14
    } yield {
      val candidatosMap = candidatos.map(c => (c.id.get, c)).toMap
      val e14ConReportesInvalidosMap = e14ConReportesInvalidos.map(e => (e.id.get, e)).toMap
      agrupados.map { case (e14Id, reportes) =>
        val groupByCandidato = reportes.groupBy{case(_, detalle) => detalle.candidatoId}
        val groupByCandidatoYVotos = groupByCandidato.map{case (candId, reportesDetalle) =>
          val detallesGroupedByVotos = reportesDetalle.groupBy{case(_, det) => det.votosSospechoso}
          val detallesGroupedByVotosConteo = detallesGroupedByVotos.map{case (votosSospechosos, seqReportes) =>
            (votosSospechosos, VotosReportadosCount(seqReportes.size, seqReportes.map(DetalleReporteStats.tupled)))
          }
          val detallesGroupedByVotosModa = detallesGroupedByVotosConteo.maxBy(_._1)

          val candidato = candidatosMap.get(candId).getOrElse(Candidato("Candidato no encontrado"))

          (candidato, DetallesGroupedByVotos(detallesGroupedByVotosModa._1, detallesGroupedByVotosModa._2, detallesGroupedByVotosConteo.toSeq.map(VotosReportadosDetalle.tupled)))
        }
        val e14 = e14ConReportesInvalidosMap.get(e14Id).get
        (e14, groupByCandidatoYVotos)
      }
    }
  }

  def votosReportadosByCandidato :  Future[ResumenSumatoria]= {

    for {
      candidatos <- candidatoDao.all()
      data <- agrupadosPorCandidatoYVoto
    } yield {
      val zero = candidatos.map(c => (c, 0)).toMap
      val sumaPorCandidato = data.foldLeft(zero){ case (acc, (e14, porCandidato)) =>
        val conVotos = porCandidato.map{ case (k, v) => (k, v.votosReportados) }
        acc |+| conVotos
      }
      val statsDetalleE14 = data
        .map{case (k, v) => (k, v.toSeq.map(StatsDetalleCandidato.tupled))}
        .toSeq
        .map(StatsDetallesE14.tupled)
      ResumenSumatoria(sumaPorCandidato.toSeq.map(StatsResumenCandidato.tupled), statsDetalleE14)
    }
  }
}
