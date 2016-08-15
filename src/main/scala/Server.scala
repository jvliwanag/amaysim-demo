package me.jvliwanag.amaysimdemo

import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._
import scalaz.concurrent._

import Encoders._

object Server {

  object PageNumberQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Long]("page")

  def createHttpService(
    pageSize: Long,
    repo: ProductRepo
  ): HttpService = HttpService {
    case GET -> Root / "products" :? PageNumberQueryParamMatcher(page) =>
      repo.getAll().flatMap { ps =>
        val pl = ProductList(ps, pageNumber = page.getOrElse(1), pageSize = pageSize)

        Ok(pl)
      }
    case GET -> Root / "products" / codeStr =>
      repo.findByCode(ProductCode(codeStr)).flatMap {
        case Some(p) =>
          Ok(p)
        case None =>
          NotFound()
      }
  }

  def createServer(
    pageSize: Long,
    repo: ProductRepo
  )(
    port: Int,
    host: String
  ): Task[server.Server] = {
    val apiService = createHttpService(pageSize, repo)

    BlazeBuilder.bindHttp(port, host).mountService(apiService).start
  }
}
