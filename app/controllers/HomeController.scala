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


  //Twitter stuff
//  val API_KEY = "j6uL99U6ha4MWfkFeDn9MBp4E"
//  val API_SECRET = "uDzej1FAg8F3fZy7bUH3NctCKquiH1ujFxodjIJ77Q1kgau2hM"
//  val CALLBACK_URL = "oob"


//  def twitterRequestToken_ = Action {
//    val url = "https://api.twitter.com/oauth/request_token"
//
//    val header = Request.generateHeader(url)
//    Logger.debug(s"twitterRequestToken header $header")
//    val response = wsClient.url(url)
//      .withHttpHeaders("Authorization" -> header, "" -> "")
//      .post(EmptyBody)
//
//    response.onComplete{ tresponse =>
//      tresponse match {
//        case Success(resp) =>
//          Logger.debug(s"Success ${resp.status}")
//          Logger.debug(s"Success ${resp.body}")
//        case Failure(error) =>
//          Logger.error("Error", error)
//      }
//    }
//
//    val token = "nothing"
////    val token = Request.read(url)
//    Logger.debug(s"Token from Twitter $token")
//    Ok(token)
//  }


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
