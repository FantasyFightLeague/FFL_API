package com.ffl.api.Framework

import org.json4s.Formats

import scala.language.implicitConversions
import scalaz.\/

trait Atomizer[A, B] { def serialize: B }
trait DeAtomizer { def deserialize[A <: AnyRef](implicit formats: Formats, manifest: Manifest[A]): A }

object JSONAtomizers {
  import org.json4s._
  import org.json4s.native.Serialization.{read, write}
  implicit val formats = DefaultFormats + org.json4s.ext.UUIDSerializer
  implicit def byteArrayTo_A(data: String): DeAtomizer =
    new DeAtomizer {
      override def deserialize[A <: AnyRef](implicit formats: Formats, manifest: Manifest[A]): A = read[A](data)
    }
  implicit def A_ToByteArray[A <: AnyRef](a: A)(implicit formats: Formats, manifest: Manifest[A]): Atomizer[A, String] =
    new Atomizer[A, String] {
      override def serialize: String = write(a)
    }
}
