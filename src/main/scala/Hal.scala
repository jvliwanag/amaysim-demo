package me.jvliwanag.amaysimdemo

import black.door.hate._, HalRepresentation.HalRepresentationBuilder
import java.net.URI

trait HalEncodable[T] { self =>
  def asHalRepresentation(v: T): HalRepresentation =
    representationBuilder(v).build()

  def asHalResource(v: T): HalResource = new HalResource {
    override def location(): URI =
      self.location(v)

    override def representationBuilder(): HalRepresentationBuilder =
      self.representationBuilder(v)
  }

  def location(v: T): URI
  def representationBuilder(v: T): HalRepresentationBuilder
}

object HalEncodable {
  def apply[T](f1: T => URI)(f2: T => HalRepresentationBuilder): HalEncodable[T] = new HalEncodable[T] {

    def location(v: T): URI = f1(v)
    def representationBuilder(v: T): HalRepresentationBuilder = f2(v)
  }

  def of[T](implicit r: HalEncodable[T]): HalEncodable[T] = r

  def asHalResource[T](v: T)(implicit r: HalEncodable[T]): HalResource =
    r.asHalResource(v)

  def asHalRepresentation[T](v: T)(implicit r: HalEncodable[T]): HalRepresentation =
    r.asHalRepresentation(v)
}
