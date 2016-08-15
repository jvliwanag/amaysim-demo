package me.jvliwanag.amaysimdemo

import black.door.hate.HalRepresentation

trait HalRepresentable[T] {
  def asHalRepresentation(v: T): HalRepresentation
}

object HalRepresentable {
  def apply[T](f: T => HalRepresentation): HalRepresentable[T] = new HalRepresentable[T] {
    def asHalRepresentation(v: T): HalRepresentation = f(v)
  }

  def of[T](implicit r: HalRepresentable[T]): HalRepresentable[T] = r
}
