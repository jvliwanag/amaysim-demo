package me.jvliwanag.amaysimdemo

import argonaut.Json
import org.http4s.client.blaze.SimpleHttp1Client
import org.http4s.argonaut._
import scalaz.concurrent.Task

object Client {
  private val client = SimpleHttp1Client()

  def getProducts(baseUrl: BaseUrl, page: Long): Task[Json] =
    client.expect[Json](baseUrl / "products")

  def getProduct(baseUrl: BaseUrl, code: ProductCode): Task[Json] =
    client.expect[Json](baseUrl / s"products/${code.value}")

  def shutdown: Task[Unit] =
    client.shutdown
}

case class BaseUrl private(value: String) extends AnyVal {
  def /(suffix: String): String =
    value + suffix
}

object BaseUrl {
  def fromString(value: String): Option[BaseUrl] = {
    var b = value

    if (!b.startsWith("http"))
      b = "http://" + b

    if (!b.endsWith("/"))
      b = b + "/"

    scala.util.Try(new java.net.URL(b))
      .toOption
      .map(u => BaseUrl(u.toExternalForm()))
  }
}
