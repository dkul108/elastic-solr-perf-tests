package jmperf;

import io.vavr.Lazy;
import io.vavr.collection.Array;
import io.vavr.collection.Iterator;
import io.vavr.control.Try;
import org.apache.jmeter.samplers.SampleResult;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static jmperf.Common.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

public class Elastic extends Scenario<SearchSourceBuilder, SearchResponse> {

    public static final Elastic elastic = new Elastic();

    protected static final Lazy<Client> client = Lazy.of(() ->
            Try.of(() -> connect()).get());

    private static TransportClient connect() throws UnknownHostException {
        return connect(ES_HOST, ES_PORT);
    }

    private static TransportClient connect(final String host,
                                           final int port) throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(
                        InetAddress.getByName(host), port));
    }

    @Override
    public SearchResponse search(final SampleResult result,
                                 final boolean cache,
                                 final SearchSourceBuilder query) {
        return searchGuide(result, cache, query);
    }

    public SearchResponse searchGuide(final SampleResult result,
                                      final boolean cache,
                                      final SearchSourceBuilder query) {
        final SearchRequestBuilder request = client.get()
                .prepareSearch(guide).setTypes(guide)
                .setRequestCache(cache).setSource(query);
        result.setSentBytes(request.toString().getBytes().length);
        result.sampleStart();
        final SearchResponse response = request.get();
        result.sampleEnd();
        result.setBytes(response.toString().getBytes().length);
        return response;
    }

    private static SearchSourceBuilder query(final QueryBuilder... filters) {
        return SearchSourceBuilder.searchSource().query(and(boolQuery(), filters));
    }

    private static SearchSourceBuilder query(final QueryBuilder[] a,
                                             final QueryBuilder... b) {
        return query(and(a, b));
    }

    private static SearchSourceBuilder and(final SearchSourceBuilder a,
                                           final QueryBuilder... b) {
        return a.query(and(boolQuery().filter(a.query()), b));
    }

    private static QueryBuilder and(final BoolQueryBuilder a,
                                    final QueryBuilder... b) {
        return constantScoreQuery(Iterator.of(b)
                .foldLeft(a, (q, f) -> q.filter(f)));
    }

    private static QueryBuilder[] and(final QueryBuilder[] a,
                                      final QueryBuilder... b) {
        return Array.of(a).appendAll(Array.of(b))
                .toJavaArray(QueryBuilder.class);
    }

    private static QueryBuilder channel(final String... channels) {
        return termsQuery(channelId_kw, channels);
    }

    private static QueryBuilder start(final long time) {
        return rangeQuery(start).gt(time);
    }

    private static QueryBuilder end(final long time) {
        return rangeQuery(end).lt(time);
    }

    private static QueryBuilder[] rightNow(final long time) {
        return new QueryBuilder[]{rangeQuery(start).lt(time),
                rangeQuery(end).gt(time)};
    }

    private static QueryBuilder category(final String... category) {
        return termsQuery(contentCategory_kw, category);
    }

    private static QueryBuilder genre(final String... genre) {
        return termsQuery(contentGenre_kw, genre);
    }

    private static QueryBuilder packageName(final String... packageName) {
        return termsQuery(contentPackage_kw, packageName);
    }

    private static QueryBuilder country(final String... country) {
        return termsQuery(contentCountry_kw, country);
    }

    private static QueryBuilder concurrency(final int limit) {
        return termQuery(contentConcurrency, limit);
    }

    /**
     * given a channel Id and a starttime and endtime<br/> return the airings between those times in order of air-times<br/> execute the above query with
     * multiple channelIds
     */
    @Override
    public SearchSourceBuilder useCase1(final long start, final long end,
                                        final String... channel) {
        return query(channel(channel), start(start), end(end)).sort(Common.start);
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times execute the above query with multiple channelIds
     */
    @Override
    public SearchSourceBuilder useCase2(final int count, final long now,
                                        final String... channel) {
        return query(channel(channel), start(now)).sort(start).size(count);
    }

    /**
     * given a filter query, return the airing and channel which matches. eg. If I say the filter is on the Category field and the value is Movie, return all
     * the airings on now which have the category movie, sorted by channel number.
     */
    @Override
    public SearchSourceBuilder useCase3(final long now, final String category) {
        return query(rightNow(now), category(category)).sort(channelId_kw);
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    @Override
    public SearchSourceBuilder useCase4(final String... channel) {
        return useCase1(pastDays(3), futureDays(14), channel);
    }
}
