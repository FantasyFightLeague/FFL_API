package com.ffl.macros

import java.util.UUID

import com.mongodb.DBObject

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

import scala.language.{implicitConversions, higherKinds}
/**
 * Project: com.fullfacing.ticketing.macros
 * Created on 2015/05/26.
 * ryno aka lemonxah -
 * https://github.com/lemonxah
 * http://stackoverflow.com/users/2919672/lemon-xah
 */
trait Mappable[A,B,C] {
  def toDBType(a: A): B
  def fromDBType(c: C): A
}


import com.mongodb.DBObject

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

trait AnotherMongoMappable[A] extends Mappable[A, DBObject, DBObject]
object AnotherMongoMappable {
  implicit def materializeMappable[A]: AnotherMongoMappable[A] = macro materializeMappableImpl[A]

  def materializeMappableImpl[A: c.WeakTypeTag](c: whitebox.Context): c.Expr[AnotherMongoMappable[A]] = {
    import c.universe._
    val tpe = weakTypeOf[A]
    val companion = tpe.typeSymbol.companion

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor ⇒ m
    }.get.paramLists.head

    c.Expr[AnotherMongoMappable[A]] { q"""
      new MongoMappable[$tpe] {
        def toDBType(t: $tpe): DBObject = grater[$tpe[_]].asDBObject(t)
        def fromDBType(dbo: DBObject): $tpe = grater[$tpe[_]].asObject(dbo)
      }
    """ }
  }
}

trait MongoMappable[A] extends Mappable[A, DBObject, DBObject]
object MongoMappable {
  implicit def materializeMappable[A]: MongoMappable[A] = macro materializeMappableImpl[A]

  def materializeMappableImpl[A: c.WeakTypeTag](c: whitebox.Context): c.Expr[MongoMappable[A]] = {
    import c.universe._
    val tpe = weakTypeOf[A]
    val companion = tpe.typeSymbol.companion

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor ⇒ m
    }.get.paramLists.head

    c.Expr[MongoMappable[A]] { q"""
      new MongoMappable[$tpe] {
        def toDBType(t: $tpe): DBObject = grater[$tpe].asDBObject(t)
        def fromDBType(dbo: DBObject): $tpe = grater[$tpe].asObject(dbo)
      }
    """ }
  }
}

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

/**
 * Project: dbabspro
 * Created on 2015/07/08.
 * ryno aka lemonxah -
 * https://github.com/lemonxah
 * http://stackoverflow.com/users/2919672/lemon-xah
 */
trait GenericMappable[A] extends Mappable[A, Map[String, Any], Map[String, Any]]
object GenericMappable {
  implicit def materializeMappable[T]: GenericMappable[T] = macro materializeMappableImpl[T]

  def materializeMappableImpl[T: c.WeakTypeTag](c: whitebox.Context): c.Expr[GenericMappable[T]] = {
    import c.universe._
    val tpe = weakTypeOf[T]
    val companion = tpe.typeSymbol.companion

    val fields = tpe.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor ⇒ m
    }.get.paramLists.head

    val (toMapParams, fromMapParams) = fields.map { field ⇒
      val name = field.name.toTermName
      val decoded = name.decodedName.toString
      val returnType = tpe.decl(name).typeSignature

      (q"$decoded → t.$name", q"map($decoded).asInstanceOf[$returnType]")
    }.unzip

    c.Expr[GenericMappable[T]] { q"""
      new GenericMappable[$tpe] {
        def toDBType(t: $tpe): Map[String, Any] = Map(..$toMapParams)
        def fromDBType(map: Map[String, Any]): $tpe = $companion(..$fromMapParams)
      }
    """ }
  }
}
