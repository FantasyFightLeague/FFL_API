package com.ffl.api.Actors.UserAuthentication

import akka.actor.Props
import com.ffl.api.DAO.MongoConnectionContext
import com.mongodb.casbah.MongoDB
import spray.routing.HttpServiceActor
import com.ffl.api.Authentication.BasicAuthentication
import scala.concurrent.ExecutionContext.Implicits._
import scala.language.postfixOps
/**
 * Package: com.ffl.api.Actors.UserAuthentication
 * Created by zandrewitte on 2015/09/30.
 */
object UserAuthenticationApiActor{ def props = Props[UserAuthenticationApiActor]}

class UserAuthenticationApiActor(val _db: MongoDB) extends HttpServiceActor with BasicAuthentication{

  val userAuthHttpService = new UserAuthenticationHttpService(_db)

  override def receive: Receive = runRoute( pathPrefix("userAuthService") {
    authenticate(basicUserAuthenticator) { authInfo =>
      userAuthHttpService.routes
    }
  })

}
