package core.util

import models._
import play.api.libs.json.Json

case class DetalleReporteJson(candidatoId: Int, votosSospechosos: Int)
case class ReporteE14Json(e14Id: Int, valido: Boolean, detalles: Seq[DetalleReporteJson] = Nil)

trait JsonFormats {

  implicit val usersFormat = Json.format[User]
  implicit val commandsFormat = Json.format[Command]
  implicit val categorysFormat = Json.format[Category]
  implicit val rankingsFormat = Json.format[Ranking]
  implicit val modelIdFormat = Json.format[ModelId]



  implicit val E14Format = Json.format[E14]
  implicit val CandidatoFormat = Json.format[Candidato]
  implicit val DetalleReporteFormat = Json.format[DetalleReporteJson]
  implicit val ReporteFormat = Json.format[ReporteE14Json]
}

object JsonFormats extends JsonFormats {

}
