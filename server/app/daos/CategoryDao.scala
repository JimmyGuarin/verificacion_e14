package daos
import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class CategoryDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  private val Categorys = TableQuery[CategorysTable]

  def all(): Future[Seq[Category]] = db.run(Categorys.result)

  def insert(category: Category): Future[Unit] = db.run(Categorys += category).map { _ => () }


  private class CategorysTable(tag: Tag) extends Table[Category](tag, "Category") {

    def id = column[Long] ("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")


    def * = (id, name,description) <> (Category.tupled, Category.unapply)
  }
}
