package core.util

import models._
import play.api.libs.json.Json

case class DetalleReporteJson(candidatoId: Int, votosSospechosos: Int)
case class ReporteE14Json(e14Id: Int, valido: Boolean, captchaToken: Option[String] = None,
                          detalles: Seq[DetalleReporteJson] = Nil)

case class RespuestaCaptcha(success: Boolean)

case class TwitterToken(oauth_token: String, oauth_token_secret: String)

case class GTokenResponse(access_token: String, token_type: Option[String],
                          expires_in: Option[Int], refresh_token: Option[String])

case class GUserInfo(id: String, email: String, name: String)


trait JsonFormats {

  implicit val E14Format = Json.format[E14]
  implicit val CandidatoFormat = Json.format[Candidato]
  implicit val DetalleReporteFormat = Json.format[DetalleReporteJson]
  implicit val ReporteFormat = Json.format[ReporteE14Json]
  implicit val DeptoFormat = Json.format[Departamento]
  implicit val MunicipioFormat = Json.format[Municipio]

  implicit val twitterTokenFormat = Json.format[TwitterToken]

  implicit val RespuestaCaptchaFormat = Json.format[RespuestaCaptcha]

  implicit val gTokenResponseFormat = Json.format[GTokenResponse]

  implicit val gUserInfo = Json.format[GUserInfo]

}

object JsonFormats extends JsonFormats {

}
