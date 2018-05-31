package models

case class Command (title: String, value: String, description: String, UserID: Long, CategoryID:Long, id: Option[Long])

