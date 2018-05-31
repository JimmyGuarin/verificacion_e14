package controllers.api

import javax.inject.Singleton
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class CommandsControllerApi@javax.inject.Inject()(cc: ControllerComponents, commandsDao: CammandDao)
                                                 (implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  import core.util.JsonFormats._

  def allCommands = Action.async {
    val fCommands = commandsDao.all()
    fCommands.map(commands => Ok(Json.toJson(commands)))
  }
}
