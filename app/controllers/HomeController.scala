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
                                            val configuration: Configuration, errorHandler: HttpErrorHandler,
                                            wsClient: WSClient, val loginService: LoginService,
                                            val usuariosDao: UsuarioDao)(implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) with Secured{


  def index = Action {
    Ok(views.html.index())
  }

  def reactApp: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(configuration.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else index
  }

  def signIn(code: String) = Action.async { implicit rs =>
    val response = (for {
      tokenResponse <- EitherT(loginService.connectWithGoogle(code))
      userInfo <- EitherT(loginService.getUserInfo(tokenResponse.access_token))
      usuario <- EitherT(loginService.getOrCreateUser(userInfo))
    } yield {
      authTokenResult(usuario)
    }).run
    response.map(_.fold(CustomResponse.errorResult, identity))
  }
}
