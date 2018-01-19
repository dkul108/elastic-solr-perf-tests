package bench

import java.time.{LocalDateTime, ZoneOffset}

import scala.util.Random

object Common {

  def rndChan = "CHAN_" + Random.nextInt(490)

  def channels = Seq(Common.rndChan, Common.rndChan)

  def timeMs(time: LocalDateTime) = time.toInstant(ZoneOffset.UTC).toEpochMilli.toString

  def now() = timeMs(LocalDateTime.now(ZoneOffset.UTC))

}