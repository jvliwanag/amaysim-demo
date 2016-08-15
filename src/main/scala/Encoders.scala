package me.jvliwanag.amaysimdemo

import org.http4s._
import org.http4s.headers._
import scodec.bits.ByteVector

trait Encoders {
  implicit def halEntityEncoder[T: HalRepresentable](implicit
    charset: Charset = DefaultCharset
  ): EntityEncoder[T] = {
    val header = `Content-Type`(MediaType.`application/hal+json`, charset)
    EntityEncoder.simple(header)(s =>
      ByteVector.view(
        HalRepresentable.of[T]
          .asHalRepresentation(s)
          .serialize()
          .getBytes(charset.nioCharset)
      )
    )
  }
}

object Encoders extends Encoders
