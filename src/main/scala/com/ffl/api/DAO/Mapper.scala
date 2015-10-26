package com.ffl.api.DAO

import com.mongodb.DBObject
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
/**
 * Package: com.ffl.api.DAL.DAO
 * Created by zandrewitte on 2015/10/26.
 */
object Mapper{

  trait Mappable[A,B,C] {
    def toDBType(a: A): B
    def fromDBType(c: C): A
  }

  trait MongoMappable[A] extends Mappable[A, DBObject, DBObject]

  def MongoMapper[A <: AnyRef](implicit manifest: Manifest[A]) = {
    new MongoMappable[A] {
      def toDBType(t: A): DBObject = grater[A].asDBObject(t)
      def fromDBType(dbo: DBObject): A = grater[A].asObject(dbo)
    }
  }

}
