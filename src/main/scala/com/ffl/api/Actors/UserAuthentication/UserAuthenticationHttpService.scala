package com.ffl.api.Actors.UserAuthentication

import akka.actor.ActorContext
import com.ffl.api.DAO.{ MongoContext, FFLMongoDAO}
import com.ffl.api.Entity.EUser
import com.ffl.api.Model.{ResponseContainer, User, UserDetail}
import com.mongodb.BasicDBObject
import com.mongodb.casbah.MongoDB
import spray.http.StatusCodes
import akka.util.Timeout
import spray.routing.HttpService
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz._, Scalaz._
import com.ffl.api.Common.ResponseConverter._
import com.ffl.api.Framework.JSONAtomizers._
import com.mongodb.casbah.Imports._
/**
 * Package: com.ffl.api.Actors.UserAuthentication
 * Created by zandrewitte on 2015/09/30.
 */
class UserAuthenticationHttpService(val _db: MongoDB)(implicit ac: ActorContext)extends HttpService with MongoContext{
  def actorRefFactory = ac
  implicit val timeout = Timeout(60 seconds)
  import FFLMongoDAO._

  val routes = Login ~ GetById ~ Register ~ Update ~ PasswordReset

  def Login = path("login"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        val u = entity.deserialize[User]

        users.headOption((EUser.email === u.email) &&
          (EUser.password === u.password.getOrElse(""))) match {
          case Some(user) => ResponseContainer(User(user.email, None, user.id), ResponseCodes.Ok)
          case None => ResponseContainer(None, ResponseCodes.Ok, s"No user found with email : ${u.email} and password: ${u.password.getOrElse("")}")
        }
      }
    }
  }

  def GetById = path("getById" / Segment){ id =>
    get{
      users.headOption(EUser.id === id.some) match {
        case Some(user) => ResponseContainer(UserDetail(User(user.email, user.password.some, user.id), user.firstName, user.lastName, user.dateOfBirth, user.status), ResponseCodes.Ok, "OK")
        case None => ResponseContainer(None, ResponseCodes.Ok, s"No user found with ID : $id")
      }
    }
  }

  def Register = path("register"){
    post {
      extract {
        _.request.entity.asString
      } { entity =>
        val u = entity.deserialize[UserDetail]
        users.headOption((EUser.email === u.user.email) && (EUser.id === u.user.id)) match {
          case Some(existingUser) => val savedUser = users += EUser(u.user.email, u.firstName, u.lastName, u.user.password.getOrElse(""), u.dateOfBirth, u.status, u.user.id)
            ResponseContainer(u.copy( user = u.user.copy(id = savedUser.id)), ResponseCodes.Ok, "User Registered")
          case None => ResponseContainer(None, ResponseCodes.BadRequest, "User already exists")
        }

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
