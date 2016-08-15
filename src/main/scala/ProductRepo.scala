package me.jvliwanag.amaysimdemo

import scalaz.concurrent.Task

trait ProductRepo {
  def getAll(): Task[Seq[Product]]
  def findByCode(code: ProductCode): Task[Option[Product]]
}

object ProductRepo {
  def fixed(entries: Seq[Product]): ProductRepo =
    new FixedProductRepo(entries)
}

class FixedProductRepo(entries: Seq[Product])
    extends ProductRepo {

  override def getAll(): Task[Seq[Product]] = {
    Task.now(entries)
  }

  override def findByCode(code: ProductCode): Task[Option[Product]] = Task {
    entries.find(_.code == code)
  }
}
