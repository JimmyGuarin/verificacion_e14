package daos

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject

import models.Ranking
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scalaz._, Scalaz._
import core.CustomResponse._


class RankingDao  @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._
  private val Rankings = TableQuery[RankingsTable]

  def all(): Future[Seq[Ranking]] = db.run(Rankings.result)

  def insert(ranking: Ranking): Future[ApiResponsez[String]] = db.run(Rankings += ranking).map { _ => "Guardado correctamente".right }

  private class RankingsTable(tag: Tag) extends Table[Ranking](tag, "Ranking") {

    def id = column[Long] ("id", O.PrimaryKey, O.AutoInc)
    def User_id = column[Long]("User_id")
    def Command_id = column[Long]("Command_id")


    def * = (User_id, Command_id, id.?) <> (Ranking.tupled, Ranking.unapply)
  }
}
