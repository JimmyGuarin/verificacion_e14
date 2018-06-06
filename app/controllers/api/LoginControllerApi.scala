package controllers.api

import javax.inject.Singleton

import core.CustomResponse
import core.util.UsuarioJson
import daos.{ReporteE14Dao, UsuarioDao}
import play.api.Configuration
import play.api.mvc.{AbstractController, ControllerComponents}
import services.LoginService

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

@Singleton
class LoginControllerApi @javax.inject.Inject()(cc: ControllerComponents, val loginService: LoginService,
                                                val usuariosDao: UsuarioDao, val configuration: Configuration,
                                                reportesDao: ReporteE14Dao)
                                               (implicit val executionContext: ExecutionContext)
  extends AbstractController(cc) with Secured{

  import core.util.JsonFormats._

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

  def userInfo = Action.async { implicit rs =>
    authenticated(rs){ userRequest =>
      CustomResponse.asyncResultz{

        reportesDao
          .statsUsuario(userRequest.usuario)
          .map{ case (total, sosp) =>
            UsuarioJson(userRequest.usuario.name, userRequest.usuario.email, total, sosp).right
          }
      }
    }
  }
}
