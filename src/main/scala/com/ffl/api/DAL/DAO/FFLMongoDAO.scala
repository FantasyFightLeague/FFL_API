package com.ffl.api.DAL.DAO

import com.ffl.api.Entity.EUser
import com.ffl.macros.{MongoMappable, Mappable}
import com.mongodb.DBObject
import com.novus.salat._
import com.mongodb.DBObject
import com.novus.salat.annotations._
import com.novus.salat.global.ctx.typeHintStrategy
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import scala.language.implicitConversions
/**
 * Package: com.fullfacing.dsl.DAO
 * Created by zandrewitte on 2015/10/08.
 */
object FFLMongoDAO {
  def apply[A <: Model](coll: MongoCollection): MongoDAO[A] = new MongoDAO[A](coll)

  // USER AUTHENTICATION
  implicit val mapUser: Mappable[EUser, DBObject, DBObject] = implicitly[MongoMappable[EUser]]

  implicit def EUserToModelWithId(u: EUser): ModelWithId[EUser] = new ModelWithId[EUser] {
    def copy(id: Option[String]) = u.copy(id = id)
  }

}
