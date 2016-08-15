package me.jvliwanag.amaysimdemo

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._
import scalaz.concurrent._

import Encoders._

object Server {
  def createHttpService(repo: ProductRepo): HttpService = HttpService {
    case GET -> Root / "products" =>

      Ok("")
    case GET -> Root / "products" / codeStr =>
      repo.findByCode(ProductCode(codeStr)).flatMap {
        case Some(p) =>
          Ok(p)
        case None =>
          NotFound()
      }
  }

  def createServer(
    repo: ProductRepo
  )(
    port: Int,
    host: String
  ): Task[server.Server] = {
    val apiService = createHttpService(repo)

    BlazeBuilder.bindHttp(port, host).mountService(apiService).start
  }
}
