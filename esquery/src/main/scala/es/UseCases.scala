package es

import java.net.InetAddress
import java.time.LocalDateTime
import java.time.ZoneOffset

import org.elasticsearch.action.search.SearchRequestBuilder
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.index.query.{BoolQueryBuilder, QueryBuilder}
import org.elasticsearch.rest.RestStatus
import org.elasticsearch.search.SearchHits
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object UseCases /* extends App */ {

  lazy val client = new PreBuiltTransportClient(Settings.EMPTY)
    .addTransportAddress(new InetSocketTransportAddress(
      InetAddress.getByName("localhost"), 9300))

  def showHit(hits: SearchHits) = {
    for (hit <- hits.getHits) println(hit.getSource)
    println("\ncount: " + hits.totalHits)
  }

  def show(search: SearchRequestBuilder) =
    showHit(search.get.getHits)

  def timeMs(time: LocalDateTime) = time.toInstant(ZoneOffset.UTC).toEpochMilli.toString

  def airingsBetween(start: LocalDateTime, end: LocalDateTime) = boolQuery()
    .filter(rangeQuery("start").from(timeMs(start)))
    .filter(rangeQuery("end").to(timeMs(end)))

  def onNow() = boolQuery().filter(rangeQuery("start")
    .from(timeMs(LocalDateTime.now())))

  def onChannel(channels: String*) =
    termsQuery("channelId.keyword", channels: _*)

  def onCategory(categories: String*) =
    termsQuery("content.category.keyword", categories: _*)

  def onGenre(genres: String*) =
    termsQuery("content.genre.keyword", genres: _*)

  /**
    * given a channel Id and a starttime and endtime
    * return the airings between those times in order of air-times
    * execute the above query with multiple channelIds
    */
  def useCase1(start: LocalDateTime, end: LocalDateTime,
               channels: Seq[String], queries: QueryBuilder*) =
    client.prepareSearch("guide1")
      .setQuery(filterBy(queries: _*)(airingsBetween(start, end)
        .filter(onChannel(channels: _*)))
      ).addSort("start", SortOrder.ASC)

  /**
    * given a channelId return the current airing next n airings in order of air-times
    * * execute the above query with multiple channelIds
    */
  def useCase2(count: Int, channels: Seq[String], queries: QueryBuilder*) =
    client.prepareSearch("guide1")
      .setQuery(filterBy(queries: _*)(
        onNow.filter(onChannel(channels: _*))))
      .setSize(count)
      .addSort("start", SortOrder.ASC)

  /**
    * given a filter query, return the airing and channel which matches.
    *eg. If I say the filter is on the Category field and the value is Movie, return all the airings on now which have the category movie, sorted by channel number.
    */
  def useCase3(queries: QueryBuilder*) =
    client.prepareSearch("guide1")
      .setQuery(queries.foldLeft(onNow)(_.filter(_)))
      .addSort("channelId.keyword", SortOrder.ASC)

  /**
    * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
    */
  def useCase4(channel: String, queries: QueryBuilder*) =
    client.prepareSearch("guide1")
      .setQuery(filterBy(queries: _*)(airingsBetween(
        LocalDateTime.now().minusDays(3),
        LocalDateTime.now().plusDays(14))
        .filter(onChannel(channel)))
      ).addSort("start", SortOrder.ASC)

  /**
    * for any of the above queries, filter the results by any given restriction on the packages array.
    *eg. by a Package Name (Europe) */

  def filterBy(queries: QueryBuilder*)(search: BoolQueryBuilder) =
    queries.foldLeft(search)(_.filter(_))

  def byPackage(name: String) = termsQuery("content.packages.name.keyword", name)

  /** by a country (the airing should have a package with a country array having a given country) */

  def byCountry(country: String) = termsQuery("content.packages.countries.keyword", country)

  /* by a concurrency limit */
  def byConcurrency(limit: Int) = termsQuery("content.packages.contract.concurrency", limit)

  /**
    * be able to paginate results
    */

  def pagesBy(pageSize: Int)(search: => SearchRequestBuilder): Iterator[SearchHits] = {
    val response = search.setSize(pageSize).get()
    val pages = (response.getHits.totalHits / pageSize)
    Iterator.single(response.getHits) ++ Iterator.tabulate(pages.intValue)(page =>
      search.setSize(pageSize).setFrom((page + 1) * pageSize).get().getHits)
  }

  def showPages(search: SearchRequestBuilder) = for {
    r <- pagesBy(5)(search).take(10).zipWithIndex
  } {
    println(s"============ page ${r._2 + 1} ==================")
    showHit(r._1)
  }

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

  def check(query: SearchRequestBuilder) = {
    while (query.get().status() != RestStatus.OK) {}
  }

  def measure[T](n: String)(f: => T)(implicit times: Int = 1): Unit =
    showTime(n, times)(perform(f, times))

  lazy val UseCase1 = useCase1(
    LocalDateTime.parse("2017-11-05T10:00:50"),
    LocalDateTime.parse("2017-11-05T22:00:00"),
    Seq("CHAN_45", "CHAN_46"))

  lazy val UseCase1_2 = useCase1(
    LocalDateTime.parse("2017-11-05T10:00:50"),
    LocalDateTime.parse("2017-11-05T22:00:00"),
    Seq("CHAN_45", "CHAN_46"), onGenre("Action"))

  lazy val UseCase2 = useCase2(3, Seq("CHAN_45", "CHAN_46"))
  lazy val UseCase3 = useCase3(onCategory("Series"), onGenre("Action"))
  lazy val UseCase4 = useCase4("CHAN_45")
  lazy val UseCase5 = useCase4("CHAN_45", byCountry("ru"))

  lazy val useCases = Seq(
    "Warm" -> UseCase1,
    "1" -> UseCase1,
    "1.2" -> UseCase1_2,
    "2" -> UseCase2,
    "3" -> UseCase3,
    "4" -> UseCase4,
    "5" -> UseCase5
  )

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
