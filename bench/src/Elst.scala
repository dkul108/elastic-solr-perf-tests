package bench

import java.net.InetAddress
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class Elst {

  @Benchmark
  def useCase1 = {
    Elst.useCase1(
      LocalDateTime.parse("2017-11-05T10:00:50"),
      LocalDateTime.parse("2017-11-05T22:00:00"),
      Common.channels).get()
  }

  @Benchmark
  def useCase2 = {
    Elst.useCase2(10, Common.channels).get()
  }

  @Benchmark
  def useCase4 = {
    Elst.useCase4(3, 14, Common.channels).get()
  }

}

object Elst {

  lazy val client = new PreBuiltTransportClient(Settings.EMPTY)
    .addTransportAddress(new InetSocketTransportAddress(
      InetAddress.getByName("localhost"), 9300))

  def guide() = client.prepareSearch("guide1")

  def airingsBetween(start: LocalDateTime, end: LocalDateTime)
                    (bq: BoolQueryBuilder) = bq
    .filter(rangeQuery("start").from(Common.timeMs(start)))
    .filter(rangeQuery("end").to(Common.timeMs(end)))

  def onNow(bq: BoolQueryBuilder) = bq
    .filter(rangeQuery("start")
      .from(Common.now()))

  def onChannel(channels: Seq[String]) =
    boolQuery().filter(termsQuery(
      "channelId.keyword", channels: _*))

  /**
    * given a channel Id and a starttime and endtime
    * return the airings between those times in order of air-times
    * execute the above query with multiple channelIds
    */
  def useCase1(start: LocalDateTime,
               end: LocalDateTime,
               channels: Seq[String]) =
    guide.setQuery(airingsBetween(start, end)
    (onChannel(channels)))
      .addSort("start", SortOrder.ASC)

  /**
    * ﻿given a channelId return the current airing next n airings in order of air-times
    * * execute the above query with multiple channelIds
    */
  def useCase2(count: Int, channels: Seq[String]) =
    guide.setQuery(onNow(onChannel(channels)))
      .setSize(count)
      .addSort("start", SortOrder.ASC)

  /**
    * given a filter query, return the airing and channel which matches.
    *eg. If I say the filter is on the Category field and the value is Movie, return all the airings on now which have the category movie, sorted by channel number.
    */

  /**
    * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
    */
  def useCase4(before: Int, after: Int, channels: Seq[String]) =
    guide.setQuery(airingsBetween(
      LocalDateTime.now().minusDays(before),
      LocalDateTime.now().plusDays(after))
    (onChannel(channels))
    ).addSort("start", SortOrder.ASC)

  /**
    * for any of the above queries, filter the results by any given restriction on the packages array.
    *eg. by a Package Name (Europe) */

  /** by a country (the airing should have a package with a country array having a given country) */

  /* by a concurrency limit */

}