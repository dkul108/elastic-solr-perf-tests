package perf;

import io.vavr.Lazy;
import io.vavr.collection.Iterator;
import io.vavr.control.Try;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;
import org.openjdk.jmh.annotations.Benchmark;

import java.net.UnknownHostException;

import static perf.Common.*;

public class Solr implements Scenario<SolrQuery, SolrDocumentList> {

    private static final Lazy<SolrClient> client = Lazy.of(() ->
            Try.of(() -> connect()).get());

    private static SolrClient connect() throws UnknownHostException {
        return new HttpSolrClient.Builder(
                System.getProperty("perf.solr.url",
                        "http://localhost:8983/solr"))
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }

    @Override
    public SolrDocumentList search(final boolean cacheHint, final SolrQuery query)
            throws Exception {
        return client.get().query(guide, query).getResults();
    }

    private static SolrQuery query(final String... queries) {
        return new SolrQuery(Iterator.of(queries).mkString(" AND "));
    }

    private static String termsQuery(final String term, final String... terms) {
        return "(" + Iterator.of(terms).map(t -> term + ":\"" + t + "\"")
                .mkString(" OR ") + ")";
    }

    private static String channel(final String... channels) {
        return termsQuery(channelId, channels);
    }

    private static String category(final String... category) {
        return termsQuery(contentCategory, category);
    }

    private static String packageName(final String... packageName) {
        return termsQuery(contentPackage, packageName);
    }

    private static String country(final String... country) {
        return termsQuery(contentCountry, country);
    }

    private static String concurrency(final int limit) {
        return termsQuery(contentConcurrency, "" + limit);
    }

    private static String between(final long start, final long end) {
        return "(" + "start:[" + start + " TO " + end + "] AND " +
                "end:[" + start + " TO " + end + "])";
    }

    private static String start(final long time) {
        return "start:[" + time + " TO * ]";
    }

    private static String end(final long time) {
        return "end:[ * TO " + time + "]";
    }

    private static String rightNow(final long time) {
        return "(" + "start:[ *  TO " + time + "] AND " +
                "end:[" + time + " TO *])";
    }

    /**
     * given a channel Id and a starttime and endtime<br/>
     * return the airings between those times in order of air-times<br/>
     * execute the above query with multiple channelIds
     */
    @Override
    public SolrQuery useCase1(final long start, final long end,
                              final String... channel) {
        return query(channel(channel), between(start, end))
                .addSort(Common.start, SolrQuery.ORDER.asc);
    }

    @Benchmark
    public SolrDocumentList useCase1() throws Exception {
        return runUseCase1();
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times
     * execute the above query with multiple channelIds
     */
    @Override
    public SolrQuery useCase2(final int count, final long now,
                              final String... channel) {
        return query(channel(channel), start(now))
                .addSort(Common.start, SolrQuery.ORDER.asc)
                .setRows(count);
    }

    @Benchmark
    public SolrDocumentList useCase2() throws Exception {
        return runUseCase2();
    }

    /**
     * given a filter query, return the airing and channel which matches.
     * eg. If I say the filter is on the Category field and the value is Movie,
     * return all the airings on now which have the category movie, sorted by channel number.
     */
    @Override
    public SolrQuery useCase3(final long now, final String category) {
        return query(category(category), rightNow(now))
                .addSort(channelId, SolrQuery.ORDER.asc);
    }

    @Benchmark
    public SolrDocumentList useCase3() throws Exception {
        return runUseCase3();
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    @Override
    public SolrQuery useCase4(final String... channels) {
        return query(channel(channels), start(pastDays(3)), end(futureDays(14)));
    }

    @Benchmark
    public SolrDocumentList useCase4() throws Exception {
        return runUseCase4();
    }

    /**
     * for any of the above queries, filter the results by any given restriction on the packages array.
     * eg. by a Package Name (Europe)
     */
    @Override
    public SolrQuery useCase5(final String packageName, final SolrQuery query) {
        return query(packageName(packageName), query.getQuery());
    }

    @Benchmark
    public SolrDocumentList useCase5() throws Exception {
        return runUseCase5();
    }

    /**
     * by a country (the airing should have a package with a country array having a given country)
     */
    @Override
    public SolrQuery useCase6(final String country, final SolrQuery query) {
        return query(country(country), query.getQuery());
    }

    @Benchmark
    public SolrDocumentList useCase6() throws Exception {
        return runUseCase6();
    }

    /* by a concurrency limit */
    @Override
    public SolrQuery useCase7(final int limit, final SolrQuery query) {
        return query(concurrency(limit), query.getQuery());
    }

    @Benchmark
    public SolrDocumentList useCase7() throws Exception {
        return runUseCase7();
    }

}
