package core.util

import models._
import play.api.libs.json.Json

case class DetalleReporteJson(candidatoId: Int, votosSospechosos: Int)
case class ReporteE14Json(e14Id: Int, valido: Boolean, detalles: Seq[DetalleReporteJson] = Nil)

case class TwitterToken(oauth_token: String, oauth_token_secret: String)

trait JsonFormats {

  implicit val E14Format = Json.format[E14]
  implicit val CandidatoFormat = Json.format[Candidato]
  implicit val DetalleReporteFormat = Json.format[DetalleReporteJson]
  implicit val ReporteFormat = Json.format[ReporteE14Json]
  implicit val DeptoFormat = Json.format[Departamento]
  implicit val MunicipioFormat = Json.format[Municipio]

  implicit val twitterTokenFormat = Json.format[TwitterToken]
}

object JsonFormats extends JsonFormats {

}
