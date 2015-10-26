package com.ffl.api.Model

import com.ffl.api.Common.ResponseConverter.ResponseCodes
import com.mongodb.DBObject

/**
 * Package: com.ffl.api.Model
 * Created by zandrewitte on 2015/10/24.
 */
case class ResponseContainer[A](value: A, statusCode: Int = 200, responseMessage: String = "OK")
