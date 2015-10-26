package com.ffl.api.Common

import com.ffl.api.Model.ResponseContainer
import spray.http.StatusCodes
import spray.routing.StandardRoute
import spray.routing.directives.RouteDirectives._
import scala.language.implicitConversions
import com.ffl.api.Framework.JSONAtomizers._
/**
 * Package: com.fullfacing.ticketing.api.Messaging.Implicits
 * Created by zandrewitte on 2015/09/01.
 */
object ResponseConverter {

  object ResponseCodes extends Enumeration {
    type ResponseCodes = Value

    val Ok = Value(200)
    val NoContent = Value(201)
    val BadRequest = Value(400)
    val Unauthorized = Value(401)
    val Forbidden = Value(403)
    val NotFound = Value(404)
    val InternalError = Value(500)

    def list = this.values.map(a => a.toString).toList
    def byName(s: String): Option[ResponseCodes.Value] = this.values.find(_.toString == s)

    implicit def ValueToInt(responseCode: ResponseCodes.Value): Int = responseCode.id

  }

  implicit def messageToHTTP[A](ResponseContainer: ResponseContainer[A])(implicit manifest: Manifest[A]): StandardRoute = complete(messageMap.getOrElse(ResponseContainer.statusCode,StatusCodes.BadRequest), ResponseContainer.serialize)

  implicit def errorToHTTP(e: Throwable): StandardRoute = complete(StatusCodes.InternalServerError, ResponseContainer("", ResponseCodes.InternalError.id, s"Error Occurred : ${e.getMessage}").serialize)

  private val messageMap = Map(
    200 -> StatusCodes.OK,
    201 -> StatusCodes.NoContent,
    400 -> StatusCodes.BadRequest,
    401 -> StatusCodes.Unauthorized,
    403 -> StatusCodes.Forbidden,
    404 -> StatusCodes.NotFound,
    500 -> StatusCodes.InternalServerError
  )

}
