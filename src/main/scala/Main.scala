package me.jvliwanag.amaysimdemo

import org.http4s.server.ServerApp
import org.http4s.server.{Server => HServer}
import scalaz.concurrent.Task

object Main extends ServerApp {

  val DEFAULT_HOST = "127.0.0.1"
  val DEFAULT_PORT = 9999
  val DEFAULT_PAGE_SIZE = 2l

  case class Config(
    // server
    host: String = DEFAULT_HOST,
    port: Int = DEFAULT_PORT,
    pageSize: Long = DEFAULT_PAGE_SIZE
  )

  val parser = new scopt.OptionParser[Config]("amaysim-demo") {
    head("amaysim-demo", "0.1")

    opt[String]('H', "host").action((x, c) =>
      c.copy(host = x)
    ).text(s"default: $DEFAULT_HOST")

    opt[Int]('p', "port").action((x, c) =>
      c.copy(port = x)
    ).text(s"default: $DEFAULT_PORT")

    opt[Long]("page-size").action((x, c) =>
      c.copy(pageSize = x)
    ).text(s"default: $DEFAULT_PAGE_SIZE")

    help("help").text("prints this usage text")
  }


  def server(args: List[String]): Task[HServer] = {
    println("args: " + args)
    parser.parse(args, Config()) match {
      case Some(c) =>
        createServer(c)
      case None =>
        Task.fail(new Exception("Invalid config"))
    }
  }

  private def createServer(config: Config): Task[HServer] = {
    val entries: Seq[Product] = EntryLoader.unsafeReadEntries()
    val repo: ProductRepo = ProductRepo.fixed(entries)

    Server.createServer(
      pageSize = config.pageSize,
      repo = repo
    )(config.port, config.host)
  }
}
