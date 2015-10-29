package com.ffl.api.DAO

import com.ffl.api.DAO.Mapper.Mappable
import com.mongodb.{BasicDBObject, DBObject}
import com.mongodb.casbah.Imports._
import scala.language.{higherKinds, implicitConversions}

trait ModelWithId[A] { def copy(id: Option[String]):A }
trait Model { def id: Option[String] }
trait DAO[A <: Model, B, C, Q] {
  def insert(a: A)(implicit m: Mappable[A, B, C], ev: A => ModelWithId[A]): A
  def list(limit: Int = 0)(implicit m: Mappable[A, B, C]): Vector[A]
  def filter(query: DBObject, limit: Int = 0)(implicit m: Mappable[A, B, C]): Vector[A]
  def headOption(query: DBObject, limit: Int = 0)(implicit m: Mappable[A, B, C]): Option[A]
  def update(a: A)(implicit m: Mappable[A, B, C])
  def delete(a: A)(implicit m: Mappable[A, B, C])
  // SHORTHAND NOTATION
  def -=(a: A)(implicit m: Mappable[A, B, C]) = delete(a)(m)
  def +=(a: A)(implicit m: Mappable[A, B, C], ev: A => ModelWithId[A]): A = insert(a)(m, ev)
  def :=(a: A)(implicit m: Mappable[A, B, C]) = update(a)(m)
}

object Operation extends Enumeration {
  type Operation = Value

  val Equals = Value("$eq")
  val GreaterThan = Value("$gt")
  val GreaterThanOrEqual = Value("$gte")
  val LessThan = Value("$lt")
  val LessOrEqual = Value("$lte")
  val NotEqual = Value("$ne")
  val In = Value("$in")
  val Regex = Value("$regex")

}

object Operator extends Enumeration{
  type Operator = Value

  val Or = Value("$or")
  val And = Value("$and")
}

case class DBColumn[A](name: String) {
  def ===(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.Equals.toString, SingleValue(value).get)))
  def >(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.GreaterThan.toString, SingleValue(value).get)))
  def >=(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.GreaterThanOrEqual.toString, SingleValue(value).get)))
  def <(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.LessThan.toString, SingleValue(value).get)))
  def <=(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.LessOrEqual.toString, SingleValue(value).get)))
  def !==(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.NotEqual.toString, SingleValue(value).get)))
  def =!=(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.In.toString, ListValue(value).get)))
  def <*>(value: A): DBQuery = DBQuery(new BasicDBObject(this.name, new BasicDBObject(Operation.Regex.toString, SingleValue(value).get)))
}

case class DBQuery(operator1: DBObject){
  def &&(operation2: DBObject): DBQuery = DBQuery($and(operator1, operation2))
  def ||(operation2: DBObject): DBQuery = DBQuery($or(operator1, operation2))
}

trait Value[+A] { def get: A }
case class SingleValue[A](a: A) extends Value[A] { override def get: A = a }
case class ListValue[A](a: A) extends Value[A] { override def get: A = a }