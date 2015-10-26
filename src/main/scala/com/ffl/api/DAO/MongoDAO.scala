package com.ffl.api.DAO

import java.util.UUID

import com.ffl.api.DAO.Mapper.Mappable
import com.mongodb.DBObject
import com.mongodb.casbah.Imports._

import scala.language.implicitConversions
import scalaz.Scalaz._

class MongoDAO[A <: Model](coll: MongoCollection) extends DAO[A, DBObject, DBObject, DBObject] {

  override def list(limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Vector[A] = {
    coll.find().limit(limit).toVector.map(m.fromDBType)
  }

  override def filter(query: DBObject, limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Vector[A] =
    coll.find(query).limit(limit).toVector.map(m.fromDBType)

  override def headOption(query: DBObject, limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Option[A] =
    coll.find(query).limit(limit).toVector.map(m.fromDBType).headOption

  override def insert(a: A)(implicit m: Mappable[A, DBObject, DBObject], ev: A => ModelWithId[A]): A = {
    val aa = a.copy(id = UUID.randomUUID().toString.some)
    coll.insert(m.toDBType(aa))
    aa
  }

  override def update(a: A)(implicit m: Mappable[A, DBObject, DBObject]) {
    coll.update("id" $eq a.id.get, m.toDBType(a))
  }

  override def delete(a: A)(implicit m: Mappable[A, DBObject, DBObject]) {
    coll.findAndRemove("id" $eq a.id.get)
  }
}