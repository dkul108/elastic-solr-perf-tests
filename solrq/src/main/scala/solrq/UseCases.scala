package solrq

import java.time.{LocalDateTime, ZoneOffset}

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.common.params.SolrParams

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object UseCases /* extends App */ {

  lazy val client = new HttpSolrClient.Builder(
    "http://localhost:8983/solr")
    .withConnectionTimeout(10000)
    .withSocketTimeout(60000)
    .build()

  def guide(q: SolrParams) = client.query("guide", q).getResults

  def timeMs(time: LocalDateTime) = time.toInstant(ZoneOffset.UTC).toEpochMilli

  /**
    * given a channel Id and a starttime and endtime
    * return the airings between those times in order of air-times
    * execute the above query with multiple channelIds
    */
  def useCase1(start: LocalDateTime, end: LocalDateTime,
               channels: Seq[String]) = new SolrQuery(
    and(onChannels(channels), betweenAirings(start, end))
  ) addSort("start", SolrQuery.ORDER.asc)

  def and(queries: String*) = queries.mkString(" AND ")

  def onChannels(channels: Seq[String]) = "(" +
    channels.map("channelId:\"" + _ + "\"").mkString(" OR ") + ")"

  def betweenAirings(start: LocalDateTime, end: LocalDateTime) =
    "(" + "start:[" + timeMs(start) + " TO " + timeMs(end) + "] AND " +
      "end:[" + timeMs(start) + " TO " + timeMs(end) + "])"

  lazy val UseCase1 = useCase1(
    LocalDateTime.parse("2017-11-05T10:00:50"),
    LocalDateTime.parse("2017-11-05T22:00:00"),
    Seq("CHAN_45", "CHAN_46"))

  /**
    * given a channelId return the current airing next n airings in order of air-times
    * * execute the above query with multiple channelIds
    */
  def useCase2(count: Int, channels: Seq[String]) = new SolrQuery(onNow)
    .addSort("start", SolrQuery.ORDER.asc).setRows(count)

  def onNow = "start:[" + timeMs(LocalDateTime.now(ZoneOffset.UTC)) + " TO * ]"

  lazy val UseCase2 = useCase2(3, Seq("CHAN_45", "CHAN_46"))

  /**
    * given a filter query, return the airing and channel which matches.
    *eg. If I say the filter is on the Category field and the value is Movie, return all the airings on now which have the category movie, sorted by channel number.
    */

  /**
    * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
    */

  def useCase4(channelId: String) = new SolrQuery(
    betweenAirings(
      LocalDateTime.now(ZoneOffset.UTC).minusDays(3),
      LocalDateTime.now(ZoneOffset.UTC).plusDays(14)
    )).addSort("start", SolrQuery.ORDER.asc)
  
  lazy val UseCase4 = useCase4("CHAN_45")

  /**
    * for any of the above queries, filter the results by any given restriction on the packages array.
    *eg. by a Package Name (Europe) */

  /** by a country (the airing should have a package with a country array having a given country) */

  /* by a concurrency limit */

  /**
    * be able to paginate results
    */

  def showTime[T](n: String, times: Int)(f: => T): Unit = {
    val time = (((0 to 10).map { _ =>
      val s = System.currentTimeMillis()
      val r = f
      System.currentTimeMillis() - s
    }).sum / 10)
    val perf = if (time > 0) ((times * 1000) / time) else 0
    println(s"$n: $time ms , $perf/s")
  }

  import scala.concurrent.ExecutionContext.Implicits.global

  def perform[T](f: => T, times: Int): Unit =
    Await.ready(Future.sequence((0 to times).map(_ => Future(f).recover {
      case x: Throwable => x.printStackTrace()
    })), Duration.Inf)

  def measure[T](n: String)(f: => T)(implicit times: Int = 1): Unit =
    showTime(n, times)(perform(f, times))

  /*
  if (args.length == 0) println("use: <count>") else {
    implicit val times = args(0).toInt

    measure("Warm") {
      println(client.connectedNodes())
    }(1)

    for ((name, query) <- useCases)
      measure(s"Use case $name") {
        check(query)
      }
  }
  */

}
