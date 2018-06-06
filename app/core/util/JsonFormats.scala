package core.util

import models.{DetalleReporteSospechoso, _}
import play.api.libs.json.Json

case class DetalleReporteJson(candidatoId: Int, votosSospechosos: Int)
case class ReporteE14Json(e14Id: Int, valido: Boolean, captchaToken: Option[String] = None,
                          detalles: Seq[DetalleReporteJson] = Nil)

case class RespuestaCaptcha(success: Boolean)

case class TwitterToken(oauth_token: String, oauth_token_secret: String)

case class GTokenResponse(access_token: String, token_type: Option[String],
                          expires_in: Option[Int], refresh_token: Option[String])

case class GUserInfo(id: String, email: String, name: String)

//Stats
case class DetalleReporteStats(reporte: ReporteE14, detalle: DetalleReporteSospechoso)
case class VotosReportadosCount(cantReportes: Int,  reportes: Seq[DetalleReporteStats])
case class VotosReportadosDetalle(votos: Int, votosReportados: VotosReportadosCount)
case class DetallesGroupedByVotos(votosReportados: Int, reportesDetalles: VotosReportadosCount, votosReportadosDetalle: Seq[VotosReportadosDetalle])
case class StatsResumenCandidato(candidato: Candidato, votos: Int)
case class StatsDetalleCandidato(candidato: Candidato, detalle: DetallesGroupedByVotos)
case class StatsDetallesE14(e14: E14, statsDetalleCandidato: Seq[StatsDetalleCandidato])
case class ResumenSumatoria(resumen:  Seq[StatsResumenCandidato], detalles: Seq[StatsDetallesE14])

trait JsonFormats {

  implicit val E14Format = Json.format[E14]
  implicit val CandidatoFormat = Json.format[Candidato]
  implicit val DetalleReporteFormat = Json.format[DetalleReporteJson]
  implicit val ReporteFormat = Json.format[ReporteE14Json]
  implicit val DeptoFormat = Json.format[Departamento]
  implicit val MunicipioFormat = Json.format[Municipio]
  implicit val reporteE14Format = Json.format[ReporteE14]
  implicit val detalleReporteSospechosoFormat = Json.format[DetalleReporteSospechoso]


  implicit val twitterTokenFormat = Json.format[TwitterToken]
  implicit val RespuestaCaptchaFormat = Json.format[RespuestaCaptcha]
  implicit val gTokenResponseFormat = Json.format[GTokenResponse]
  implicit val gUserInfo = Json.format[GUserInfo]
  implicit val usuarioFormat = Json.format[Usuario]


  implicit val detalleReporteStats = Json.format[DetalleReporteStats]
  implicit val votosReportadosCount = Json.format[VotosReportadosCount]

  implicit val votosReportadosDetalle = Json.format[VotosReportadosDetalle]
  implicit val detallesGroupedByVotos = Json.format[DetallesGroupedByVotos]

  implicit val statsResumenCandidato = Json.format[StatsResumenCandidato]

  implicit val statsDetalleCandidato = Json.format[StatsDetalleCandidato]

  implicit val statsDetallesE14 = Json.format[StatsDetallesE14]

  implicit val resumenSumatoria = Json.format[ResumenSumatoria]
}

object JsonFormats extends JsonFormats {

}
