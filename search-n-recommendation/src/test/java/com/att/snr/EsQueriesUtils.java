package com.att.snr;


import io.vavr.collection.Iterator;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

interface EsQueriesUtils {

    static RangeQueryBuilder onNow(String fieldName) {
        return rangeQuery(fieldName).from(LocalDateTime.now().toString());
    }

    static BoolQueryBuilder airingsTimeRange(LocalDateTime start, LocalDateTime end) {
        return boolQuery().
                filter(rangeQuery("start").from(start.toString())).
                filter(rangeQuery("end").to(end.toString()));
    }

    static RangeQueryBuilder range(String fieldName, LocalDateTime from, LocalDateTime to) {
        return rangeQuery(fieldName).
                gte(from.toString()).
                lte(to.toString());
    }

    static RangeQueryBuilder until(String fieldName, LocalDateTime to) {
        return rangeQuery(fieldName).lte(to.toString());
    }

    static TermsQueryBuilder categories(String ...values) {
        return termsQuery("content.category.keyword", values);
    }

    static TermsQueryBuilder channels(Collection<String> values) {
        return termsQuery("channelId.keyword", values);
    }

    static TermsQueryBuilder byPackage(String ...names ) {
        return termsQuery("content.packages.name.keyword", names);
    }

    /** by a country (the airing should have a package with a country array having a given country) */
    static TermsQueryBuilder byCountry(String ...countries) {
        return termsQuery("content.packages.countries.keyword", countries);
    }

    /** by a concurrency limit */
    static TermsQueryBuilder byConcurrency(int ... limits) {
        return termsQuery("content.packages.contract.concurrency", limits);
    }

    static void checkOkStatus(SearchRequestBuilder query ) {
        while (query.get().status() != RestStatus.OK) {}
    }

    static void showPages(final SearchRequestBuilder requestBuilder) {
        final AtomicInteger pageNo = new AtomicInteger(0);
        pagesBy(5, () -> requestBuilder).take(10).forEach(searchHits -> showResults(pageNo.incrementAndGet(), searchHits));
    }

    static void showResults(SearchRequestBuilder requestBuilder) {
        showResults(1, requestBuilder.get().getHits());
    }

    static void showResults(int pageNo, SearchHits hits) {
        System.out.println("page number:" + pageNo);
        hits.forEach(hit -> System.out.println(hit.getSourceAsString()));
        System.out.println("total records count:" + hits.getTotalHits());
    }

    static Iterator<SearchHits> pagesBy(final int pageSize, final Supplier<SearchRequestBuilder> searchRequest) {
        final SearchRequestBuilder searchRequestBuilder = searchRequest.get();
        final SearchResponse response = searchRequestBuilder.setSize(pageSize).get();
        final int pages =  (int)(response.getHits().getTotalHits() / pageSize);
        return Iterator.of(response.getHits()).<SearchHits>concat(
                Iterator.<SearchHits>tabulate(getNextPagesNumber(response, pageSize, pages), page ->
                        searchRequestBuilder.setSize(pageSize).setFrom((page + 1) * pageSize).get().getHits()
                )
        );
    }

    static int getNextPagesNumber(SearchResponse response, int pageSize, int pages) {
        return response.getHits().getTotalHits() > pageSize ? pages : pages - 1;
    }
}
