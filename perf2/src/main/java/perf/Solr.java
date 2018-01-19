package perf;

import io.vavr.Lazy;
import io.vavr.collection.Iterator;
import io.vavr.control.Try;
import model.Guide;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocumentList;

import java.net.UnknownHostException;

import static perf.Common.*;

public class Solr extends Scenario<SolrQuery, SolrDocumentList> {

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
        return client.get().query("core02", query).getResults();
    }

    private static SolrQuery query(final String... queries) {
        return new SolrQuery("*:*").setFilterQueries(queries);
    }

    private static SolrQuery query(final Iterator<String> queries) {
        return query(queries.toJavaArray(String.class));
    }

    private static SolrQuery query(final Iterator<String> queries, final String... queries2) {
        return query(queries.concat(Iterator.of(queries2)));
    }

    private static SolrQuery query(final Iterator<String>... queries) {
        return query(Iterator.concat(queries));
    }

    private static SolrQuery query(final SolrQuery query, final String... queries) {
        return query.addFilterQuery(queries);
    }

    private static SolrQuery query(final SolrQuery query, final Iterator<String> queries) {
        return query(query, queries.toJavaArray(String.class));
    }

    private static SolrQuery query(final SolrQuery query, final Iterator<String>... queries) {
        return query(query, Iterator.concat(queries));
    }

    private static SolrQuery query(final SolrQuery query, final Iterator<String> queries, final String... queries2) {
        return query(query, queries.concat(Iterator.of(queries2)));
    }

    private static Iterator<String> termsQuery(final String name, final String... values) {
        return Iterator.of(Iterator.of(values).flatMap(v ->
                termQuery(name, v)).mkString(" OR "));
    }

    private static Iterator<String> termQuery(final String name, final String value) {
        return Iterator.of(name + ":" + value);
    }

    private static Iterator<String> rangeQuery(final String name, final String from, final String to) {
        return Iterator.of(name + ":[" + from + " TO " + to + "]");
    }

    private static Iterator<String> channel(final String... channels) {
        return termsQuery(channelId, channels);
    }

    private static Iterator<String> category(final String... category) {
        return termsQuery(contentCategory, category);
    }

    private static Iterator<String> packageName(final String... packageName) {
        return termsQuery(contentPackage, packageName);
    }

    private static Iterator<String> country(final String... country) {
        return termsQuery(contentCountry, country);
    }

    private static Iterator<String> concurrency(final int limit) {
        return termQuery(contentConcurrency, "" + limit);
    }

    private static Iterator<String> start(final long time) {
        return rangeQuery(Common.start, "" + time, "*");
    }

    private static Iterator<String> end(final long time) {
        return rangeQuery(Common.end, "*", "" + time);
    }

    private static Iterator<String> rightNow(final long time) {
        return Iterator.concat(rangeQuery(Common.start, "*", "" + time),
                rangeQuery(Common.end, "" + time, "*"));
    }

    /**
     * given a channel Id and a starttime and endtime<br/> return the airings between those times in order of air-times<br/> execute the above query with
     * multiple channelIds
     */
    @Override
    public SolrQuery useCase1(final long start, final long end,
                              final String... channel) {
        return query(channel(channel), start(start), end(end))
                .addSort(Common.start, SolrQuery.ORDER.asc);
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times execute the above query with multiple channelIds
     */
    @Override
    public SolrQuery useCase2(final int count, final long now,
                              final String... channel) {
        return query(channel(channel), start(now))
                .addSort(Common.start, SolrQuery.ORDER.asc)
                .setRows(count);
    }

    /**
     * given a filter query, return the airing and channel which matches. eg. If I say the filter is on the Category field and the value is Movie, return all
     * the airings on now which have the category movie, sorted by channel number.
     */
    @Override
    public SolrQuery useCase3(final long now, final String category) {
        return query(category(category), rightNow(now))
                .addSort(channelId, SolrQuery.ORDER.asc);
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    @Override
    public SolrQuery useCase4(final String... channels) {
        return query(channel(channels), start(pastDays(3)), end(futureDays(14)));
    }

    /**
     * for any of the above queries, filter the results by any given restriction on the packages array. eg. by a Package Name (Europe)
     */
    @Override
    public SolrQuery useCase5(final String packageName, final SolrQuery query) {
        return query(query, packageName(packageName));
    }

    /**
     * by a country (the airing should have a package with a country array having a given country)
     */
    @Override
    public SolrQuery useCase6(final String country, final SolrQuery query) {
        return query(query, country(country));
    }

    /* by a concurrency limit */
    @Override
    public SolrQuery useCase7(final int limit, final SolrQuery query) {
        return query(query, concurrency(limit));
    }

    @Override
    public void insert(final Guide g) throws Exception {
        client.get().addBean("core02", g);
    }
}
