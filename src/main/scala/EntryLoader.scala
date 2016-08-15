package me.jvliwanag.amaysimdemo

import argonaut._
import scala.io.Source
import org.log4s._

object EntryLoader {
  private[this] val logger = getLogger

  def readEntries() = {
    val source = Source.fromInputStream(getClass.getResourceAsStream("/data.json"))
    val entries = try {source.mkString} finally source.close()

    val entriesDecode = DecodeJson(c => (c --\ "entries").as[Seq[Product]])
    Parse.decodeEither(entries)(entriesDecode)
  }

  def unsafeReadEntries(): Seq[Product] = readEntries().fold((err => {
    logger.warn("Unable to read entries. Using empty. Error: " + err)
    Seq.empty
  }), identity)
}
