package com.ffl.api.Model

/**
 * Package: com.ffl.api.Model
 * Created by zandrewitte on 2015/10/24.
 */
case class ResponseContainer[A](value: A, statusCode: Int = 0, responseMessage: String = "")
