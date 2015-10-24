package com.ffl.api.Actors.UserAuthentication

import akka.actor.ActorContext
import com.ffl.api.DAL.DAO.MongoContext
import com.ffl.api.Entity.EUser
import com.ffl.api.Model.{ResponseContainer, User, UserDetail}
import com.mongodb.casbah.MongoDB
import spray.http.StatusCodes
import akka.util.Timeout
import spray.routing.HttpService
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz._, Scalaz._
import com.ffl.api.Common.ResponseConverter._
/**
 * Package: com.ffl.api.Actors.UserAuthentication
 * Created by zandrewitte on 2015/09/30.
 */
class UserAuthenticationHttpService(val _db: MongoDB)(implicit ac: ActorContext)extends HttpService with MongoContext{
  def actorRefFactory = ac
  implicit val timeout = Timeout(60 seconds)
  import com.ffl.api.DAL.DAO.FFLMongoDAO._

  val routes = Login ~ GetById ~ Register ~ Update ~ PasswordReset

  def Login = path("login"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        complete(StatusCodes.OK)
      }
    }
  }

  def GetById = path("getById" / Segment){ id =>
    get{
      users.headOption(EUser.id === id.some) match {
        case Some(user) => ResponseContainer(UserDetail(User(user.email, user.password.some, user.id), user.firstName, user.lastName, user.dateOfBirth), 1, "OK")
        case None => ResponseContainer(None, 3, "FAILED")
      }
    }
  }

  def Register = path("register"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        complete(StatusCodes.OK)
      }
    }
  }

  def Update = path("update"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        complete(StatusCodes.OK)
      }
    }
  }

  def PasswordReset = path("resetPassword"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        complete(StatusCodes.OK)
      }
    }
  }

}
