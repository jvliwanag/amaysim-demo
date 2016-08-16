package me.jvliwanag.amaysimdemo

import argonaut.Json
import java.util.concurrent.CountDownLatch
import org.http4s.server.ServerApp
import org.http4s.server.{Server => HServer}
import scala.util.Properties
import scalaz.concurrent.Task

object Main {
  val DEFAULT_HOST = "127.0.0.1"
  val DEFAULT_PORT = 9999
  val DEFAULT_PAGE_SIZE = 2l
  val DEFAULT_BASE_URL = "https://amaysim-demo.herokuapp.com/"

  sealed trait Mode
  case object RunServer extends Mode
  case object GetProducts extends Mode
  case object GetProduct extends Mode

  case class Config(
    mode: Mode = RunServer,

    // server
    host: String = Properties.envOrElse("HOST", DEFAULT_HOST),
    port: Int = Properties.envOrElse("PORT", s"$DEFAULT_PORT").toInt,
    pageSize: Long = Properties.envOrElse("PAGE_SIZE", s"$DEFAULT_PAGE_SIZE").toLong,

    // client
    baseUrl: String = DEFAULT_BASE_URL,

    // get products
    page: Long = 1,

    // get product
    productCode: String = ""
  )

  val parser = new scopt.OptionParser[Config]("amaysim-demo") {
    head("amaysim-demo", "0.1")

    cmd("run-server").action((_, c) =>
      c.copy(mode = RunServer)
    ).children(
      opt[String]('H', "host").action((x, c) =>
        c.copy(host = x)
      ).text(s"default: $DEFAULT_HOST. env var: HOST"),

      opt[Int]('p', "port").action((x, c) =>
        c.copy(port = x)
      ).text(s"default: $DEFAULT_PORT. env var: PORT"),

      opt[Long]("page-size").action((x, c) =>
        c.copy(pageSize = x)
      ).text(s"default: $DEFAULT_PAGE_SIZE. env var: PAGE_SIZE")
    )

    cmd("get-products").action((_, c) =>
      c.copy(mode = GetProducts)
    ).children(
      opt[String]("base-url").action((x, c) =>
        c.copy(baseUrl = x)
      ).text(s"default: $DEFAULT_BASE_URL"),

      opt[Long]("page").action((x, c) =>
        c.copy(page = x)
      )
    )

    cmd("get-product").action((_, c) =>
      c.copy(mode = GetProduct)
    ).children(
      opt[String]("base-url").action((x, c) =>
        c.copy(baseUrl = x)
      ).text(s"default: $DEFAULT_BASE_URL"),

      opt[String]("product-code").action((x, c) =>
        c.copy(productCode = x)
      ).required()
    )


    help("help").text("prints this usage text")
  }


  def main(args: Array[String]): Unit = {
    parser.parse(args, Config()).foreach { c =>
      c.mode match {
        case RunServer =>
          runServer(c)
        case GetProducts =>
          getProducts(c)
        case GetProduct =>
          getProduct(c)
      }
    }
  }

  private def runServer(config: Config): Unit = {
    val entries: Seq[Product] = EntryLoader.unsafeReadEntries()
    val repo: ProductRepo = ProductRepo.fixed(entries)

    val latch = new CountDownLatch(1)
    val s = Server.createServer(
      pageSize = config.pageSize,
      repo = repo
    )(config.port, config.host)
      .unsafePerformSync

    latch.await()
  }

  private def getProducts(config: Config): Unit = doClient(config) { b =>
    Client.getProducts(b, page = config.page)
  }

  private def getProduct(config: Config): Unit = doClient(config) { b =>
    Client.getProduct(b, ProductCode(config.productCode))
  }

  private def doClient(config: Config)(f: BaseUrl => Task[Json]): Unit = {
    val baseUrl = BaseUrl.fromString(config.baseUrl)
    baseUrl match {
      case Some(u) =>
        f(u).map(j => println(j.spaces2))
          .onFinish(_ => Client.shutdown)
          .unsafePerformSync
      case None =>
        println(s"Invalid base url: ${config.baseUrl}")
    }
  }
}
