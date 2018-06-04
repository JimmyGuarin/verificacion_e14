package services

import javax.inject.Singleton

import core.CustomResponse
import core.util.{GTokenResponse, GUserInfo}
import daos.UsuarioDao
import models.Usuario
import play.api.{Configuration, Logger}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._

@Singleton
class LoginService @javax.inject.Inject()(wsClient: WSClient, configuration: Configuration, usuarioDao: UsuarioDao)(
  implicit executionContext: ExecutionContext){

  import core.util.JsonFormats._

  lazy val gScope = configuration.get[String]("oauth.google.scope")
  lazy val gGrantType = configuration.get[String]("oauth.google.grant_type")
  lazy val gRedirectUri = configuration.get[String]("oauth.google.redirect_url")
  lazy val gClientId = configuration.get[String]("oauth.google.client_id")
  lazy val gClientSecret = configuration.get[String]("oauth.google.client_secret")


  def connectWithGoogle(code: String): Future[CustomResponse.ApiResponsez[GTokenResponse]] = {
    val tokenResponse = wsClient.url("https://www.googleapis.com/oauth2/v4/token")
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .post(
        Map("code" -> Seq(code),
          "client_id" -> Seq(gClientId),
        "scope" -> Seq(gScope),
        "client_secret" -> Seq(gClientSecret),
        "grant_type" -> Seq(gGrantType),
        "redirect_uri" -> Seq(gRedirectUri))
      )

    tokenResponse.map { response =>
      Logger.debug(s"Respuesta de server: $response")
      Logger.debug(s"Respuesta de server: ${response.body}")
      response.json.validate[GTokenResponse].fold({ errors =>
        CustomResponse.ApiError(500, "Error en respuesta de server", errors.mkString).left
      }, _.right)
    }
  }

  def getUserInfo(token: String): Future[CustomResponse.ApiResponsez[GUserInfo]] = {
    val userInfoResponse = wsClient.url("https://www.googleapis.com/oauth2/v2/userinfo")
      .withHttpHeaders("Authorization" -> s"Bearer $token").get()

    userInfoResponse.map { response =>
      Logger.debug(s"Google user info: ${response}")
      response.json.validate[GUserInfo].fold({ errors =>
        CustomResponse.ApiError(500, "Error en respuesta de server para userInfo", errors.mkString).left
      }, _.right)
    }
  }

  def getOrCreateUser(userInfo: GUserInfo): Future[CustomResponse.ApiResponsez[Usuario]] = {
    usuarioDao.usuarioPorIdGoogle(userInfo.id).flatMap{ mUsuario =>
      mUsuario match {
        case None =>
          val usuario = Usuario(userInfo.id, userInfo.email, userInfo.name)
          usuarioDao.insertar(usuario).map(_.right)
        case Some(usuario) => Future.successful(usuario.right)
      }
    }
  }

  
}
