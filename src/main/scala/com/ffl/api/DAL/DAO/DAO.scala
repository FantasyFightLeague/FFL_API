package com.ffl.api.DAL.DAO

import com.ffl.macros.Mappable

import scala.language.higherKinds
import scala.language.implicitConversions

trait ModelWithId[A] { def copy(id: Option[String]):A }
trait Model { def id: Option[String] }
trait DAO[A <: Model, B, C, Q] {
  def interpreter(q: Query, empty: Q): Option[Q]
  def insert(a: A)(implicit m: Mappable[A, B, C], ev: A => ModelWithId[A]): A
  def list(limit: Int = 0)(implicit m: Mappable[A, B, C]): Vector[A]
  def filter(query: Query, limit: Int = 0)(implicit m: Mappable[A, B, C]): Vector[A]
  def headOption(query: Query, limit: Int = 0)(implicit m: Mappable[A, B, C]): Option[A]

  /**
   *
   * @deprecated This will be removed when sub-object query has been implemented
   */
  @Deprecated
  def customQuery(q: Q, limit: Int = 0)(implicit m: Mappable[A, B, C]): Vector[A]

  def update(a: A)(implicit m: Mappable[A, B, C])
  def delete(a: A)(implicit m: Mappable[A, B, C])
  def delete(query: Query)(implicit m: Mappable[A, B, C])
  // SHORTHAND NOTATION
  def -=(a: A)(implicit m: Mappable[A, B, C]) = delete(a)(m)
  def +=(a: A)(implicit m: Mappable[A, B, C], ev: A => ModelWithId[A]): A = insert(a)(m, ev)
  def :=(a: A)(implicit m: Mappable[A, B, C]) = update(a)(m)
}

trait Writer[A, B] { def write(a: A): B }
trait Cleaner[A, B] { def clean(a: A): B }

trait Value[+A] { def get: A }
case class SingleValue[A](a: A) extends Value[A] { override def get: A = a }
case class ListValue[+A](a: List[A]) extends Value[List[A]] { override def get: List[A] = a }

case class Field[A](name: String) {
  def asc: OrderField = OrderField(this, 1)
  def dsc: OrderField = OrderField(this, -1)
  def ===(value: A): Query = Equals(this, SingleValue(value))
  def !==(value: A): Query = NotEquals(this, SingleValue(value))
  def <(value: A): Query = LessThan(this, SingleValue(value))
  def >(value: A): Query = GreaterThan(this, SingleValue(value))
  def >=(value: A): Query = GreaterOrEqual(this, SingleValue(value))
  def <=(value: A): Query = LessOrEqual(this, SingleValue(value))
  def =!=(value: List[A]): Query = In(this, ListValue(value))
  def =!!=(value: A): Query = ElemMatchList(this, SingleValue(value))
}

object FieldExtentions {
  implicit class stringField(field: Field[String]) {
    def <%>(value: String): Query = Like(field, SingleValue(value))
    def <*>(value: String): Query = Regex(field, SingleValue(value))
  }
}

sealed trait Query { self =>
  def &&(t: Query): Query = And(self, t)
  def ||(t: Query): Query = Or(self, t)
  def orderBy(orderings: List[OrderField]): OrderBy = OrderBy(self, orderings)
}

case class OrderField(field: Field[_], order: Int)
sealed trait Operator extends Query { def left: Query; def right: Query }
case class Or(left: Query, right: Query) extends Operator
case class And(left: Query, right: Query) extends Operator
case class OrderBy(left: Query, field: List[OrderField]) extends Query
sealed trait Operand extends Query { def field: Field[_]; def value: Value[_] }
case class GreaterOrEqual[A](field: Field[A], value: SingleValue[A]) extends Operand
case class GreaterThan[A](field: Field[A], value: SingleValue[A]) extends Operand
case class LessOrEqual[A](field: Field[A], value: SingleValue[A]) extends Operand
case class LessThan[A](field: Field[A], value: SingleValue[A]) extends Operand
case class Equals[A](field: Field[A], value: SingleValue[A]) extends Operand
case class NotEquals[A](field: Field[A], value: SingleValue[A]) extends Operand
case class In[A](field: Field[A], value: ListValue[A]) extends Operand
case class ElemMatchList[A](field: Field[A], value: SingleValue[A]) extends Operand
case class Like[A](field: Field[A], value: SingleValue[A]) extends Operand
case class Regex[A](field: Field[A], value: SingleValue[A]) extends Operand


object QueryExtentions {
  implicit def itemlist[T](t: T): List[T] = List(t)
  implicit def tuple2list[T](t: (T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple3list[T](t: (T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple4list[T](t: (T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple5list[T](t: (T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple6list[T](t: (T, T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple7list[T](t: (T, T, T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple8list[T](t: (T, T, T, T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple9list[T](t: (T, T, T, T, T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
  implicit def tuple10list[T](t: (T, T, T, T, T, T, T, T, T, T)): List[T] = t.productIterator.map(_.asInstanceOf[T]).toList
}