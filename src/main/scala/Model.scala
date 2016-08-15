package me.jvliwanag.amaysimdemo

import argonaut._
import black.door.hate.HalRepresentation
import java.net.URI

case class ProductCode(value: String) extends AnyVal
case class DaysExpiry(value: Int) extends AnyVal
case class SizeMb(value: Int) extends AnyVal

case class Product(
  code: ProductCode,
  name: String,
  description: Option[String],
  price: BigDecimal,
  expiry: DaysExpiry,
  isPlan: Boolean,
  isUnlimited: Boolean,
  sizeMb: SizeMb,
  is4G: Boolean,
  isAutoRenew: Boolean,
  termsUrl: URI,
  infoUrl: URI
)

object Product {
  implicit val halRepresentable: HalRepresentable[Product] = HalRepresentable { p =>
    import p._

    HalRepresentation.builder()
      .addProperty("code", code.value)
      .addProperty("name", name)
      .addProperty("description", description.orNull)
      .addProperty("price", price)
      .addProperty("expiry", expiry.value)
      .addProperty("isPlan", isPlan)
      .addProperty("isUnlimited", isUnlimited)
      .addProperty("sizeMb", sizeMb.value)
      .addProperty("is4G", is4G)
      .addProperty("isAutoRenew", isAutoRenew)
      .addLink("terms", termsUrl)
      .addLink("info", infoUrl)
      .build()
  }

  implicit val _readProductCode: DecodeJson[ProductCode] = DecodeJson.of[String].map(ProductCode)
  implicit val _readDaysExpiry: DecodeJson[DaysExpiry] = DecodeJson.of[Int].map(DaysExpiry)
  implicit val _readSizeMb: DecodeJson[SizeMb] = DecodeJson.of[Int].map(SizeMb)
  implicit val _readUrl: DecodeJson[URI] = DecodeJson.optionDecoder(x =>
    x.string flatMap (s => DecodeJson.tryTo(URI.create(s))), "URI")

  implicit val _readProduct: DecodeJson[Product] = DecodeJson.derive[Product]

}
