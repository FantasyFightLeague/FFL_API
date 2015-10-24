package com.ffl.api.DAL.DAO

import java.util.UUID
import com.ffl.macros.Mappable
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import scala.language.implicitConversions
import scalaz._, Scalaz._

object MongoInterpreter {
  // Interpreter typeclasses
  def opmap: PartialFunction[Operand, String] = {
    case _: Equals[_] => "$eq"
    case _: GreaterThan[_] => "$gt"
    case _: GreaterOrEqual[_] => "$gte"
    case _: LessThan[_] => "$lt"
    case _: LessOrEqual[_] => "$lte"
    case _: NotEquals[_] => "$ne"
    case _: In[_] => "$in"
    case _: Regex[_] => "$regex"
  }

  implicit val MongoOperandWriter = new Writer[Operand, DBObject] {
    override def write(o: Operand): DBObject = o match {
      case Equals(f, v) => new BasicDBObject(f.name, v.get)
      case ElemMatchList(f, v) => new BasicDBObject(f.name, new BasicDBObject("$elemMatch", new BasicDBObject("$in", v.get)))
      case Like(f, v) => new BasicDBObject(f.name, new BasicDBObject("$regex", s".*${v.get}.*"))
      case op: Operand => new BasicDBObject(op.field.name, new BasicDBObject(opmap(op), op.value.get))
    }
  }

  implicit val MongoOperatorWriter = new Writer[Operator, DBObject] {
    override def write(o: Operator): DBObject = (o match
    { case _: Or => $or; case _: And => $and})(MongoQueryWriter.write(o.left),MongoQueryWriter.write(o.right))
  }

  implicit val MongoOrderByWriter = new Writer[OrderBy, DBObject] {
    override def write(order: OrderBy): DBObject =
      new BasicDBObject("$query", MongoQueryWriter.write(order.left))
        .append("$orderby", order.field.foldLeft(new BasicDBObject()) { case (a, o) => a.append(o.field.name, o.order) })
  }

  implicit val MongoQueryWriter: Writer[Query, DBObject] = new Writer[Query, DBObject] {
    override def write(a: Query): DBObject = a match {
      case op: Operand => MongoOperandWriter.write(op)
      case op: Operator => MongoOperatorWriter.write(op)
      case op: OrderBy => MongoOrderByWriter.write(op)
    }
  }
}

class MongoDAO[A <: Model](coll: MongoCollection) extends DAO[A, DBObject, DBObject, DBObject] {
  import MongoInterpreter._

  def interpreter(q: Query, empty: DBObject = MongoDBObject()): Option[DBObject]  = {
    try { Some(implicitly[Writer[Query, DBObject]].write(q)) } catch { case e: Exception => None }
  }

  override def list(limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Vector[A] = {
    coll.find().limit(limit).toVector.map(m.fromDBType)
  }

  override def filter(query: Query, limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Vector[A] = {
    interpreter(query) match {
      case Some(q) => coll.find(q).limit(limit).toVector.map(m.fromDBType)
      case None => Vector()
    }
  }

  override def headOption(query: Query, limit: Int = 0)(implicit m: Mappable[A, DBObject, DBObject]): Option[A] = {
    interpreter(query) match {
      case Some(q) => coll.find(q).limit(limit).toVector.map(m.fromDBType).headOption
      case None => None
    }
  }

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

  override def delete(query: Query)(implicit m: Mappable[A, DBObject, DBObject]): Unit = {
    interpreter(query) match {
      case Some(q) => coll.findAndRemove(q)
      case None =>
    }
  }

  /**
   *
   * @deprecated This will be removed when sub-object query has been implemented
   */
  @Deprecated
  override def customQuery(q: DBObject, limit: Int)(implicit m: Mappable[A, DBObject, DBObject]): Vector[A] = {
    coll.find(q).limit(limit).toVector.map(m.fromDBType)
  }
}