package com.ffl.api.Common

import com.ffl.api.Model.ResponseContainer
import spray.http.StatusCodes
import spray.routing.StandardRoute
import spray.routing.directives.RouteDirectives._
import scala.language.implicitConversions
import com.ffl.api.DAL.Framework.JSONAtomizers._
/**
 * Package: com.fullfacing.ticketing.api.Messaging.Implicits
 * Created by zandrewitte on 2015/09/01.
 */
object ResponseConverter {

  implicit def messageToHTTP[A](ResponseContainer: ResponseContainer[A])(implicit manifest: Manifest[A]): StandardRoute = complete(messageMap.getOrElse(ResponseContainer.statusCode,StatusCodes.BadRequest), ResponseContainer.atomized)

  implicit def anyToHTTP(a: String): StandardRoute =  complete(StatusCodes.BadRequest, ResponseContainer("", 2, s"$a").atomized)

  implicit def errorToHTTP(e: Throwable): StandardRoute = complete(StatusCodes.InternalServerError, ResponseContainer("", 0, s"Error Occurred : ${e.getMessage}").atomized)

  private val messageMap = Map(
    0 -> StatusCodes.InternalServerError,
    1 -> StatusCodes.OK,
    2 -> StatusCodes.BadRequest,
    3 -> StatusCodes.Forbidden,
    4 -> StatusCodes.NotFound,
    5 -> StatusCodes.Unauthorized
  )

}
