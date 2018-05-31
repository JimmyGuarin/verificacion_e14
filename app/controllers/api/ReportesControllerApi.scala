package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import daos.{CandidatoDao, UserDao}
import models.{E14, User, Usuario}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{ReportesService, UserService}

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

@Singleton
class ReportesControllerApi @javax.inject.Inject()(cc: ControllerComponents, reportesService: ReportesService,
                                                   candidatosDao: CandidatoDao)
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

  def guardarReporte = Action.async { implicit rs =>
    CustomResponse.asyncResultz {
      val usuario = Usuario("test", Some(1)) //TODO
      ???
    }
    ???
  }
}
