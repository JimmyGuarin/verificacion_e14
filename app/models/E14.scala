package models

case class E14(link: String, mesa: String, departamento: String, municipio: String, zona: String, puesto: String, id: Option[Int] = None)

case class Usuario(googleId: String, email: String, name: String,  id: Option[Int] = None)

case class Departamento(nombre: String, codigo: String, id: Option[Int] = None)

case class Municipio(idDepto: Int, nombre: String, codigo: String, id: Option[Int] = None)

case class Candidato(nombre: String, id: Option[Int] = None)

case class ReporteE14(e4Id: Int, usuarioId: Int, valido: Boolean, id: Option[Int] = None)

case class DetalleReporteSospechoso(reporteId: Int, candidatoId: Int, votosSospechoso: Int, id: Option[Int] = None)
