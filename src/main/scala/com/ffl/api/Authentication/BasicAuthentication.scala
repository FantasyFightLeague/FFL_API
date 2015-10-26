package com.ffl.api.Authentication

import spray.routing.authentication.{BasicAuth, UserPass}
import spray.routing.directives.AuthMagnet
import scala.concurrent.{Future, ExecutionContext}

/**
 * Package: com.ffl.api.Authentication
 * Created by zandrewitte on 2015/10/06.
 */

case class ApiUser(login: String,
                   password: String)
class AuthInfo(val user: ApiUser) {
  def hasPermission(permission: String) = {
    // Code to verify whether user has the given permission      }
  }
}
  trait BasicAuthentication {
    def basicUserAuthenticator(implicit ec: ExecutionContext): AuthMagnet[AuthInfo] = {
      def validateUser(userPass: Option[UserPass]): Option[AuthInfo] = {
        for {
          p <- userPass
          user = ApiUser(p.user, p.pass)
          if p.user == "FFL" && p.pass == "FFLP@$$w0rd"
        } yield new AuthInfo(user)
      }
      def authenticator(userPass: Option[UserPass]): Future[Option[AuthInfo]] = Future {
        validateUser(userPass)
      }
      BasicAuth(authenticator _, realm = "Private API")
    }
}
