package daos

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject

import models.E14
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class E14Dao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val E14Table = TableQuery[E14Table]

  def all(): Future[Seq[E14]] = db.run(E14Table.result)

  def getMaxId(): Future[Int] = db.run(E14Table.map(_.id).max.result).map(_.getOrElse(0))

  def totalRegistros(): Future[Int] = {
    db.run(E14Table.length.result)
  }

  def getById(e14Id: Int):  Future[Option[E14]] = {
    val result = E14Table.filter(_.id === e14Id).result
    db.run(result).map(_.headOption)
  }

  private class E14Table(tag: Tag) extends Table[E14](tag, "e14") {

    def id = column[Int] ("id", O.PrimaryKey, O.AutoInc)
    def link = column[String]("link")
    //def mesa = column[String]("mesa")
    def departamento = column[String]("departamento")
    def municipio = column[String]("municipio")
    def zona = column[String]("zona")
    def puesto= column[String]("zona")


    def * = (link, /*mesa,*/ departamento, municipio, zona, puesto, id.?) <> (E14.tupled, E14.unapply)
  }
}
