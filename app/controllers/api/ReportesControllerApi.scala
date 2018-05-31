package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import core.util.ReporteE14Json
import daos.{CandidatoDao, DepartamentoDao, MunicipioDao}
import models.{E14, Usuario}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.ReportesService

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

@Singleton
class ReportesControllerApi @javax.inject.Inject()(cc: ControllerComponents, reportesService: ReportesService,
                                                   candidatosDao: CandidatoDao, departamentoDao: DepartamentoDao,
                                                   municipioDao: MunicipioDao)
                                                  (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._


  def getRandomE14 = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      val usuario = Usuario("test", Some(1)) //TODO
      val result: EitherT[Future, CustomResponse.ApiError, E14] =
        for {
          e14 <- EitherT(reportesService.getRandomE14(usuario))
        } yield {
          e14
        }
      result.run
    }
  }

  def getCandidatos = Action.async { implicit rs =>
    candidatosDao
      .all()
      .map(candidatos => CustomResponse.apply(Right(candidatos)))
  }

  def getDepartamentos = Action.async { implicit rs =>
    departamentoDao
      .all()
      .map(departamentos => CustomResponse.apply(Right(departamentos)))
  }

  def getCiudades = Action.async { implicit rs =>
    municipioDao
      .all()
      .map(municipios => CustomResponse.apply(Right(municipios)))
  }

  def guardarReporte = Action.async(parse.json) { implicit rs =>
    CustomResponse.asyncResultz {
      val usuario = Usuario("test", Some(1)) //TODO
      val result: EitherT[Future, CustomResponse.ApiError, String] =
        for {
          reporteJson <- EitherT(CustomResponse.jsonToResponseAsync(rs.body.validate[ReporteE14Json]))
          mensaje <- EitherT(reportesService.guardarReporte(usuario, reporteJson))
        } yield {
          mensaje
        }
      result.run
    }
  }
}
