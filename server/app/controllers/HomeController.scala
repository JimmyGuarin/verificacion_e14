package controllers

import javax.inject._
import play.api.db._
import play.api.mvc.{Action, _}

import scala.concurrent.{ExecutionContext}


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @javax.inject.Inject()(db: Database, cc: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  def index = Action {
    var outString = "Number is "
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 9 as testkey ")

      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
    Ok(outString)
  }
}
