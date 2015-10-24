package com.ffl.api.Entity

import com.ffl.api.DAL.DAO.{Field, Model}
import com.ffl.macros.entity

/**
 * Package: com.ffl.api.Entity
 * Created by zandrewitte on 2015/10/24.
 */
// USER AUTHENTICATION
object EUser extends scala.AnyRef with scala.Serializable {
  object email extends Field[String]("email") {}
  object firstName extends Field[String]("firstName") {}
  object lastName extends Field[String]("lastName") {}
  object dateOfBirth extends Field[Long]("dateOfBirth") {}
  object status extends Field[Boolean]("status") {}
  object id extends Field[Option[String]]("id") {}
}

case class EUser(email: String, firstName: String, lastName: String,
                 password: String, dateOfBirth: Long,
                 status: Boolean, id: Option[String] = None) extends Model
