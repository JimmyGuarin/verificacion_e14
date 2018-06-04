package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import core.util.ReporteE14Json
import daos.{CandidatoDao, DepartamentoDao, MunicipioDao, UsuarioDao}
import models.{E14, Usuario}
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents}
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
        val result: EitherT[Future, CustomResponse.ApiError, E14] =
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

  def getCiudades = Action.async { implicit rs =>
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
}
