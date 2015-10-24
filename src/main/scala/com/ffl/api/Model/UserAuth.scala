package com.ffl.api.Model

import java.util.UUID

/**
 * Package: com.ffl.api.Model
 * Created by zandrewitte on 2015/10/15.
 */
case class User(email: String, password: Option[String], id: Option[String] = None)

case class UserDetail(user: User, firstName: String, lastName: String, dateOfBirth: Long)
