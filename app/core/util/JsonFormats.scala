package core.util

import models.{DetalleReporteSospechoso, _}
import play.api.libs.json.Json

case class DetalleReporteJson(candidatoId: Int, votosSospechosos: Int)
case class ReporteE14Json(e14Id: String, valido: Boolean, captchaToken: Option[String] = None,
                          detalles: Seq[DetalleReporteJson] = Nil)

case class RespuestaCaptcha(success: Boolean)

case class TwitterToken(oauth_token: String, oauth_token_secret: String)

case class GTokenResponse(access_token: String, token_type: Option[String],
                          expires_in: Option[Int], refresh_token: Option[String])

case class GUserInfo(id: String, email: String, name: String)

case class UsuarioJson(name: String, email: String, reportes: Int, sospechosos: Int)

//Stats
case class DetalleReporteStats(reporte: ReporteE14, detalle: DetalleReporteSospechoso)
case class VotosReportadosCount(cantReportes: Int,  reportes: Seq[DetalleReporteStats])
case class VotosReportadosDetalle(votos: Int, votosReportados: VotosReportadosCount)
case class DetallesGroupedByVotos(votosReportados: Int, reportesDetalles: VotosReportadosCount, votosReportadosDetalle: Seq[VotosReportadosDetalle])
case class StatsResumenCandidato(candidato: Candidato, votos: Int, e14Reportes: Set[E14ConReporte], cantReportes: Int)
case class StatsDetalleCandidato(candidato: Candidato, detalle: DetallesGroupedByVotos)
case class StatsDetallesE14(e14: E14, statsDetalleCandidato: Seq[StatsDetalleCandidato])
case class ResumenSumatoria(resumen:  Seq[StatsResumenCandidato], detalles: Seq[StatsDetallesE14])

case class TotalVerificados(verificados: Double)

case class DetalleReporteSospechosoDto(reporteId: Int, candidatoId: Int, votosSospechoso: Int, id: Option[Int] = None)

case class E14ConReporte(e14: E14, votosReportados: Int, cantReportes: Int)

trait JsonFormats {

  implicit val E14Format = Json.format[E14]
  implicit val E14EncriptFormat = Json.format[E14Encript]
  implicit val CandidatoFormat = Json.format[Candidato]
  implicit val DetalleReporteFormat = Json.format[DetalleReporteJson]
  implicit val ReporteFormat = Json.format[ReporteE14Json]
  implicit val DeptoFormat = Json.format[Departamento]
  implicit val MunicipioFormat = Json.format[Municipio]
  implicit val reporteE14Format = Json.format[ReporteE14]
  implicit val detalleReporteSospechosoForma = Json.format[DetalleReporteSospechoso]
  implicit val detalleReporteSospechosoFormat = Json.format[DetalleReporteSospechosoDto]

  implicit val e14ConReporteFormat = Json.format[E14ConReporte]

  implicit val twitterTokenFormat = Json.format[TwitterToken]
  implicit val RespuestaCaptchaFormat = Json.format[RespuestaCaptcha]
  implicit val gTokenResponseFormat = Json.format[GTokenResponse]
  implicit val gUserInfo = Json.format[GUserInfo]
  implicit val usuarioFormat = Json.format[UsuarioJson]


  implicit val detalleReporteStats = Json.format[DetalleReporteStats]
  implicit val votosReportadosCount = Json.format[VotosReportadosCount]

  implicit val votosReportadosDetalle = Json.format[VotosReportadosDetalle]
  implicit val detallesGroupedByVotos = Json.format[DetallesGroupedByVotos]

  implicit val statsResumenCandidato = Json.format[StatsResumenCandidato]

  implicit val statsDetalleCandidato = Json.format[StatsDetalleCandidato]

  implicit val statsDetallesE14 = Json.format[StatsDetallesE14]

  implicit val resumenSumatoria = Json.format[ResumenSumatoria]

  implicit val totalVerificados = Json.format[TotalVerificados]


}

object JsonFormats extends JsonFormats {

}
