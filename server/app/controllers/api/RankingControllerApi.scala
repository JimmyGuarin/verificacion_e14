package controllers.api

import javax.inject.Singleton
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class RankingControllerApi@javax.inject.Inject()(cc: ControllerComponents, rankingsDao: RankingDao)
                                                (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._

  def allRankings = Action.async {
    val fRankings = rankingsDao.all()
    fRankings.map(rankings => Ok(Json.toJson(rankings)))
  }
}
