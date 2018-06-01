package controllers

import javax.inject._

import core.util.TwitterToken
import play.api.{Configuration, Logger}
import play.api.db._
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.libs.ws.{EmptyBody, WSClient}
import play.api.mvc.{Action, _}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @javax.inject.Inject()(db: Database, cc: ControllerComponents, assets: Assets,
                                            config: Configuration, errorHandler: HttpErrorHandler,
                                            wsClient: WSClient)(implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._
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

  //Twitter stuff
  val API_KEY = "j6uL99U6ha4MWfkFeDn9MBp4E"
  val API_SECRET = "uDzej1FAg8F3fZy7bUH3NctCKquiH1ujFxodjIJ77Q1kgau2hM"
  val CALLBACK_URL = "oob"


  def twitterRequestToken_ = Action {
    val url = "https://api.twitter.com/oauth/request_token"

    val header = Request.generateHeader(url)
    Logger.debug(s"twitterRequestToken header $header")
    val response = wsClient.url(url)
      .withHttpHeaders("Authorization" -> header)
      .post(EmptyBody)

    response.onComplete{ tresponse =>
      tresponse match {
        case Success(resp) =>
          Logger.debug(s"Success ${resp.status}")
          Logger.debug(s"Success ${resp.body}")
        case Failure(error) =>
          Logger.error("Error", error)
      }
    }

    val token = "nothing"
//    val token = Request.read(url)
    Logger.debug(s"Token from Twitter $token")
    Ok(token)
  }
}
