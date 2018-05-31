package core.util

import models.{Category, Command, Ranking, User}
import play.api.libs.json.Json

trait JsonFormats {

  implicit val usersFormat = Json.format[User]
  implicit val commandsFormat = Json.format[Command]
  implicit val categorysFormat = Json.format[Category]
  implicit val rankingsFormat = Json.format[Ranking]

}

object JsonFormats extends JsonFormats {

}
