package bench

import java.time.{LocalDateTime, ZoneOffset}
import java.util.concurrent.TimeUnit

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.common.params.SolrParams
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class Solr {

  @Benchmark
  def useCase1 = {
    Solr.guide(Solr.useCase1(
      LocalDateTime.parse("2017-11-05T10:00:50"),
      LocalDateTime.parse("2017-11-05T22:00:00"),
      Common.channels))
  }

  @Benchmark
  def useCase2 = {
    Solr.guide(Solr.useCase2(10, Common.channels))
  }

  @Benchmark
  def useCase4 = {
    Solr.guide(Solr.useCase4(3, 14, Common.channels))
  }

}

object Solr {

  lazy val client = new HttpSolrClient.Builder(
    "http://localhost:8983/solr")
    .withConnectionTimeout(10000)
    .withSocketTimeout(60000)
    .build()

  def guide(q: SolrParams) = client.query("guide", q).getResults

  /**
    * given a channel Id and a starttime and endtime
    * return the airings between those times in order of air-times
    * execute the above query with multiple channelIds
    */
  def useCase1(start: LocalDateTime, end: LocalDateTime,
               channels: Seq[String]) = new SolrQuery(
    and(onChannels(channels),
      betweenAirings(start, end))
  ) addSort("start", SolrQuery.ORDER.asc)

  def and(queries: String*) = queries.mkString(" AND ")

  def onChannels(channels: Seq[String]) = "(" +
    channels.map("channelId:\"" + _ + "\"").mkString(" OR ") + ")"

  def betweenAirings(start: LocalDateTime, end: LocalDateTime) =
    "(" + "start:[" + Common.timeMs(start) + " TO " + Common.timeMs(end) + "] AND " +
      "end:[" + Common.timeMs(start) + " TO " + Common.timeMs(end) + "])"

  /**
    * given a channelId return the current airing next n airings in order of air-times
    * * execute the above query with multiple channelIds
    */
  def useCase2(count: Int, channels: Seq[String]) = new SolrQuery(
    and(onChannels(channels), onNow))
    .addSort("start", SolrQuery.ORDER.asc).setRows(count)

  def onNow = "start:[" + Common.now() + " TO * ]"

  /**
    * given a filter query, return the airing and channel which matches.
    *eg. If I say the filter is on the Category field and the value is Movie, return all the airings on now which have the category movie, sorted by channel number.
    */

  /**
    * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
    */

  def useCase4(before: Int, after: Int, channels: Seq[String]) = new SolrQuery(
    and(onChannels(channels),
      betweenAirings(
        LocalDateTime.now(ZoneOffset.UTC).minusDays(before),
        LocalDateTime.now(ZoneOffset.UTC).plusDays(after)
      ))).addSort("start", SolrQuery.ORDER.asc)
}