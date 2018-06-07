package controllers

import javax.inject._

import controllers.api.Secured
import core.CustomResponse
import daos.UsuarioDao
import play.api.Configuration
import play.api.db._
import play.api.http.HttpErrorHandler
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, _}
import services.LoginService

import scala.concurrent.ExecutionContext
import scalaz.Scalaz._
import scalaz._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @javax.inject.Inject()(db: Database, cc: ControllerComponents, assets: Assets,
                                            val configuration: Configuration, errorHandler: HttpErrorHandler)(implicit executionContext: ExecutionContext)
  extends AbstractController(cc){

  def reactApp: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(configuration.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else reactApp
  }

}
