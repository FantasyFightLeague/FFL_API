package com.ffl.api.Actors.UserAuthentication

import akka.actor.Props
import com.ffl.api.DAL.DAO.{MongoContext, MongoConnectionContext}
import com.mongodb.casbah.MongoDB
import spray.routing.HttpServiceActor

/**
 * Package: com.ffl.api.Actors.UserAuthentication
 * Created by zandrewitte on 2015/09/30.
 */
object UserAuthenticationApiActor{ def props = Props[UserAuthenticationApiActor]}

class UserAuthenticationApiActor(val _db: MongoDB) extends HttpServiceActor{

  val userAuthHttpService = new UserAuthenticationHttpService(_db)

  override def receive: Receive = runRoute( pathPrefix("userAuthService") {
    userAuthHttpService.routes
  })

}
