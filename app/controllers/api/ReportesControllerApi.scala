package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import core.util.{ReporteE14Json, TotalVerificados}
import daos.{CandidatoDao, DepartamentoDao, MunicipioDao, UsuarioDao}
import models.{E14, E14Encript, Usuario}
import play.api.Configuration
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{LoginService, ReportesService}

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

@Singleton
class ReportesControllerApi @javax.inject.Inject()(cc: ControllerComponents, reportesService: ReportesService,
                                                   candidatosDao: CandidatoDao, departamentoDao: DepartamentoDao,
                                                   municipioDao: MunicipioDao, val loginService: LoginService,
                                                   val usuariosDao: UsuarioDao, val configuration: Configuration)
                                                  (implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) with Secured{

  import core.util.JsonFormats._

  def getRandomE14 = Action.async { implicit rs =>
    authenticated(rs){ usuarioRequest =>
      CustomResponse.asyncResultz {
        val usuario: Usuario = usuarioRequest.usuario
        val result: EitherT[Future, CustomResponse.ApiError, E14Encript] =
          for {
            e14 <- EitherT(reportesService.getRandomE14(usuario))
          } yield {
            e14
          }
        result.run
      }
    }
  }

  def getCandidatos = Action.async { implicit rs =>
    authenticated(rs) { _ =>
      candidatosDao
        .all()
        .map(candidatos => CustomResponse.apply(Right(candidatos)))
    }
  }

  def getDepartamentos = Action.async { implicit rs =>
    authenticated(rs) { _ =>
      departamentoDao
        .all()
        .map(departamentos => CustomResponse.apply(Right(departamentos)))
    }
  }

  def getMunicipios = Action.async { implicit rs =>
    authenticated(rs) { _ =>
      municipioDao
        .all()
        .map(municipios => CustomResponse.apply(Right(municipios)))
    }
  }

  def guardarReporte = Action.async(parse.json) { implicit rs =>
    authenticated(rs) { userRequest =>
      CustomResponse.asyncResultz {
        val usuario: Usuario = userRequest.usuario
        val result: EitherT[Future, CustomResponse.ApiError, String] =
          for {
            reporteJson <- EitherT(CustomResponse.jsonToResponseAsync(rs.body.validate[ReporteE14Json]))
            _ <- EitherT(reportesService.validarCaptcha(reporteJson.captchaToken, reporteJson.valido))
            mensaje <- EitherT(reportesService.guardarReporte(usuario, reporteJson))
          } yield {
            mensaje
          }
        result.run
      }
    }
  }

  def statsPorcentajeVerificados = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      reportesService
        .porcentajeE14VerificadoFromCache
        .map(v => TotalVerificados(v).right)
    }
  }

  def statsSumatoriaResumen = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      reportesService
        .estadisticasFromCache
//        .votosReportadosByCandidato
        .map(_.right)
    }
  }

  def obtenerPDFSospechoso(detalleId: Int) = Action.async { implicit rs =>
    authenticated(rs) { _ =>
      reportesService
        .obtenerPDFSospechoso(detalleId).map { mbytes =>
        mbytes.map(bytes =>
          Ok(bytes).as("application/pdf")
        ).getOrElse(
          NotFound
        )
      }
    }
  }

  def actualizarPDFSospechosos = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      reportesService
        .actualizarLinksPdfSospechosos()
        .map(_ => "Links actualizados correctamente".right)
    }
  }

}
