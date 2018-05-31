package controllers.api

import javax.inject.Singleton
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class CategoryControllerApi@javax.inject.Inject()(cc: ControllerComponents, categoryDao: CategoryDao)
                                                 (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._

  def allCategories = Action.async {
    val fCategories = categoryDao.all()
    fCategories.map(categorys => Ok(Json.toJson(categorys)))
  }
}
