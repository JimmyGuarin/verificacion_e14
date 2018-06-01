package services

import javax.inject.Singleton

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

@Singleton
class LoginService @javax.inject.Inject()(wsClient: WSClient)(
  implicit executionContext: ExecutionContext){



}
