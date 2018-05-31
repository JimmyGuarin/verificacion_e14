package controllers

import javax.inject._

import play.api.Configuration
import play.api.db._
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @javax.inject.Inject()(db: Database, cc: ControllerComponents, assets: Assets, config: Configuration, errorHandler: HttpErrorHandler)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  def version = Action {
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

  def index = Action {
    Ok(views.html.index())
  }

  def reactApp: Action[AnyContent] = assets.at("index.html")

  def assetOrDefault(resource: String): Action[AnyContent] = if (resource.startsWith(config.get[String]("apiPrefix"))){
    Action.async(r => errorHandler.onClientError(r, NOT_FOUND, "Not found"))
  } else {
    if (resource.contains(".")) assets.at(resource) else index
  }

  def appSummary = Action {
    Ok(Json.obj("content" -> "Scala Play React Seed"))
  }


}
