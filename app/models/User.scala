package models

case class User (firstName: String, lastName: String, email:String, password:String, id: Option[Long] = None)
