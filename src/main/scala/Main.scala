package me.jvliwanag.amaysimdemo

import org.http4s.server.ServerApp
import org.http4s.server.{Server => HServer}
import scalaz.concurrent.Task

object Main extends ServerApp {

  def server(args: List[String]): Task[HServer] = {
    val entries: Seq[Product] = EntryLoader.unsafeReadEntries()
    val repo: ProductRepo = ProductRepo.fixed(entries)

    Server.createServer(
      pageSize = 2,
      repo = repo
    )(9999, "127.0.0.1")
  }
}
