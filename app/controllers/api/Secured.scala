package controllers.api

import core.CustomResponse
import daos.UsuarioDao
import models.Usuario
import play.api.mvc._
import services.LoginService
import pdi.jwt._
import play.api.{Configuration, Logger}
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.{JsObject, JsString, Json, Writes}
import play.api.mvc.Results.Ok

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

trait Secured {

  def loginService: LoginService

  def usuariosDao: UsuarioDao

  def executionContext: ExecutionContext

  def configuration: Configuration

  val TOKEN_ID = "user_id"

  implicit val ec = executionContext

  case class UserRequest[A](usuario: Usuario,
                            request: Request[A]) extends WrappedRequest[A](request)

  def authenticated[A](request: Request[A])(block: (UserRequest[A] => Future[Result])): Future[Result] = {
    withAuthorizationToken(request).flatMap{ mUsuario =>
      mUsuario
        .map(usuario => block(UserRequest(usuario, request)))
        .getOrElse{
          Future.apply(CustomResponse.errorResult(CustomResponse.ApiError(401, "Usuario invalido o no autenticado", "Usuario invalido o no autenticado")))
        }
    }
  }

  protected def withAuthorizationToken(request: RequestHeader): Future[Option[Usuario]] = {

    val result: OptionT[Future, Usuario] = for {
      userId <- OptionT(Future.apply( request.jwtSession.getAs[Int](TOKEN_ID)))
      usuario <- OptionT(usuariosDao.usuarioPorId(userId))
    } yield {
      usuario
    }
    result.run
  }


  def authTokenResult(usuario: Usuario)(
                       implicit rh: RequestHeader): Result = {

    val HEADER_NAME: String = configuration.getOptional[String]("play.http.session.jwtName").getOrElse("Authorization")
    val TOKEN_PREFIX: String = configuration.getOptional[String]("play.http.session.tokenPrefix").getOrElse("Bearer ")

    val rwjs = Ok.addingToJwtSession(TOKEN_ID -> usuario.id.get.toString)
    val session = rwjs.jwtSession
    Logger.debug(s"Secured session: ${session.toString}")

    Ok {
      Json.obj(
        HEADER_NAME -> (TOKEN_PREFIX + session.serialize))
    }.withJwtSession(session)
  }

  //************************************************************Ignore this for now*******************************************//
  //All below copied from pdi.jwt 0.6.0, IDEA hates the library for some reason
  private def sanitizeHeader(header: String): String =
    if (header.startsWith(JwtSession.TOKEN_PREFIX)) {
      header.substring(JwtSession.TOKEN_PREFIX.length()).trim
    } else {
      header.trim
    }


  private def requestToJwtSession(request: RequestHeader): JwtSession = {

    val hn = request.headers.get(JwtSession.REQUEST_HEADER_NAME)

    val sh = hn.map(sanitizeHeader)
    val dh = sh.map(JwtSession.deserialize)
    dh.getOrElse(JwtSession())
  }

  implicit class RichResult(result: Result) {
    /**
      * Retrieve the current [[JwtSession]] from the headers (first from the Result then from the RequestHeader), if none, create a new one.
      *
      * @return the JwtSession inside the headers or a new one
      */
    def jwtSession(implicit request: RequestHeader): JwtSession = {
      result.header.headers.get(JwtSession.REQUEST_HEADER_NAME) match {
        case Some(token) => JwtSession.deserialize(sanitizeHeader(token))
        case None        => requestToJwtSession(request)
      }
    }

    /**
      * If the Play app has a session.maxAge config, it will extend the expiration of the [[JwtSession]] by that time, if not, it will do nothing.
      *
      * @return the same Result with, eventually, a prolonged [[JwtSession]]
      */
    def refreshJwtSession(implicit request: RequestHeader): Result = JwtSession.MAX_AGE match {
      case None => result
      case _    => result.withJwtSession(jwtSession.refresh)
    }

    /** Override the current [[JwtSession]] with a new one */
    def withJwtSession(session: JwtSession): Result = {
      val ss = session.serialize
      result.withHeaders(JwtSession.REQUEST_HEADER_NAME -> (JwtSession.TOKEN_PREFIX + ss))
    }
    /** Override the current [[JwtSession]] with a new one created from a JsObject */
    def withJwtSession(session: JsObject): Result = withJwtSession(JwtSession(session))

    /** Override the current [[JwtSession]] with a new one created from a sequence of tuples */
    def withJwtSession(fields: (String, JsValueWrapper)*): Result = withJwtSession(JwtSession(fields: _*))

    /** Override the current [[JwtSession]] with a new empty one */
    def withNewJwtSession: Result = withJwtSession(JwtSession())

    /** Remove the current [[JwtSession]], which means removing the associated HTTP header */
    def withoutJwtSession: Result = result.copy(header = result.header.copy(headers = result.header.headers - JwtSession.REQUEST_HEADER_NAME))

    /** Keep the current [[JwtSession]] and add some values in it, if a key is already defined, it will be overriden. */
    def addingToJwtSession(values: (String, String)*)(implicit request: RequestHeader): Result = {
      val jwtS = jwtSession
      val jwtS2 = jwtS + new JsObject(values.map(kv => kv._1 -> JsString(kv._2)).toMap)
      val res = withJwtSession(jwtS2)
      res
    }

    /** Keep the current [[JwtSession]] and add some values in it, if a key is already defined, it will be overriden. */
    def addingToJwtSession[A: Writes](key: String, value: A)(implicit request: RequestHeader): Result = {
      withJwtSession(jwtSession + (key, value))
    }

    /** Remove some keys from the current [[JwtSession]] */
    def removingFromJwtSession(keys: String*)(implicit request: RequestHeader): Result = {
      withJwtSession(jwtSession -- (keys: _*))
    }
  }

  /**
    * By adding `import pdi.jwt._`, you will implicitely add this method to `RequestHeader` allowing you to easily retrieve
    * the [[JwtSession]] inside your Play application.
    *
    * {{{
    * package controllers
    *
    * import play.api._
    * import play.api.mvc._
    * import pdi.jwt._
    *
    * object Application extends Controller {
    *   def index = Action { request =>
    *     val session: JwtSession = request.jwtSession
    *   }
    * }
    * }}}
    */
  implicit class RichRequestHeader(request: RequestHeader) {
    /** Return the current [[JwtSession]] from the request */
    def jwtSession: JwtSession = requestToJwtSession(request)
  }


}
