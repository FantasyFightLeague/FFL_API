package com.ffl.api.Entity

import com.ffl.api.DAO
import com.ffl.api.DAO.Model
import com.mongodb.DBObject
import com.novus.salat._
import com.novus.salat.global._
/**
 * Package: com.ffl.api.Entity
 * Created by zandrewitte on 2015/10/24.
 */
// USER AUTHENTICATION
object EUser extends AnyRef with Serializable {
  object email extends DAO.DBColumn[String]("email") {}
  object password extends DAO.DBColumn[String]("password") {}
  object firstName extends DAO.DBColumn[String]("firstName") {}
  object lastName extends DAO.DBColumn[String]("lastName") {}
  object dateOfBirth extends DAO.DBColumn[Long]("dateOfBirth") {}
  object status extends DAO.DBColumn[Boolean]("status") {}
  object id extends DAO.DBColumn[Option[String]]("id") {}
}

case class EUser(email: String, firstName: String, lastName: String,
                 password: String, dateOfBirth: Long,
                 status: Boolean, id: Option[String] = None) extends Model