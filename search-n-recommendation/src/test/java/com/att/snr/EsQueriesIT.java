package com.att.snr;

import io.vavr.collection.Iterator;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.wavesoftware.jmh.junit.utilities.JmhCleaner;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.att.snr.EsQueriesUtils.*;
import static java.net.InetAddress.getByName;
import static org.elasticsearch.index.query.QueryBuilders.*;


//http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
//ssh -L 9300:localhost:9300 ubuntu@18.216.156.123
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EsQueriesIT {

    @ClassRule
    public static JmhCleaner cleaner = new JmhCleaner(EsQueriesIT.class);


    @Test
    public void launchBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode (Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)
                .threads(8)
                .forks(1)
                .shouldFailOnError(true)
                //.shouldDoGC(true)
                //.jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
                //.addProfiler(WinPerfAsmProfiler.class)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private Client client;

        @Setup(Level.Trial)
        public void initialize() throws UnknownHostException {
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
            client.addTransportAddress(new InetSocketTransportAddress(getByName("localhost"), 9300));
            Objects.requireNonNull(client);
            this.client = client;
        }
    }

    private static final Collection<String> channels = Lists.newArrayList("CHAN_45", "CHAN_46");
    private static final String START_TIME = "2017-11-05T10:00:50";
    private static final String END_TIME = "2017-11-05T12:00:00";

    /**
     * given a channel Id and a START_TIME time and END_TIME time
     * return the airings between those times in order of air-times
     * execute the above query with multiple channelIds
     */
    @Benchmark
    public void whenFindAiringsByChannelIdAndStartEnd_thenReturnAiringsBetweenStartEndInOrderAirTimes(BenchmarkState state) throws IOException {
        //showPages(
        checkOkStatus(
                state.client.
                    prepareSearch("guide").
                    setQuery(
                            boolQuery().
                                filter(channels(channels)).
                                filter(airingsTimeRange(LocalDateTime.parse(START_TIME), LocalDateTime.parse(END_TIME)))
                    ).
                    addSort("start", SortOrder.ASC)
        );
    }

    private static final int NUMBER_OF_NEXT_AIRINGS = 5;

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times
     * execute the above query with multiple channelIds
     */
    @Benchmark
    public void whenFindAiringsByChannelIdAndNow_thenReturnCurrentAndNAiringsInOrderAirTimes(BenchmarkState state) throws IOException {
        //showResults(
        checkOkStatus(
                state.client.prepareSearch("guide").
                        setQuery(
                                boolQuery().
                                        filter(channels(channels)).
                                        filter(onNow("start"))
                        ).
                        setSize(NUMBER_OF_NEXT_AIRINGS + 1).
                        addSort("start", SortOrder.ASC)
        );
    }


    /**
     * given a filter query, return the airing and channel which matches.
     * eg. If I say the filter is on the Category field and the value is Movie,
     * return all the airings on now which have the category movie, sorted by channel number.
     */
    @Benchmark
    public void whenFilterQueryAvailable_thenReturnAiringAndChannelOnNowByCategorySpecificValueInOrderByChannelNumber(BenchmarkState state) throws IOException {
        //showPages(
        checkOkStatus(
                state.client.
                    prepareSearch("guide").
                    setQuery(
                         boolQuery().
                            filter(channels(channels)).
                            filter(categories("Live Event")).
                            filter(onNow("start"))
                    ).
                    addSort("channelId.keyword", SortOrder.ASC)
        );
    }

    private static final int START_DAYS = 3;
    private static final int END_DAYS = 14;

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    @Benchmark
    public void givenChannelId_thenReturnAiringsUpTo3DaysPriorAnd14DaysInTheFuture(BenchmarkState state) throws IOException {
        final LocalDateTime now = LocalDateTime.now();
        //showPages(
        checkOkStatus(
                state.client.
                    prepareSearch("guide").
                    setQuery(
                            boolQuery().
                                filter(
                                    channels(
                                            Lists.newArrayList(channels.iterator().next())
                                    )
                                ).
                                filter(range("start", now.minusDays(START_DAYS), now.plusDays(END_DAYS))).
                                filter(until("end", now.plusDays(END_DAYS))).
                                filter(byCountry("mexico")).
                                filter(byConcurrency(1))
                    ).
                    addSort("start", SortOrder.ASC)
        );
    }

    /**
     * for any of the above queries, filter the results by any given restriction on the packages array.
     * eg. by a Package Name (Europe)
     * by a country (the airing should have a package with a country array having a given country)
     * by a concurrency limit
     */
    public void givenAnyPreviousQueryBuilder_thenFilterOnPackageNameCountryAndConcurrencyLimit() throws IOException {
        //in a prev test
    }

    /**
     * be able to paginate results
     */
    //added to methods except 2nd one where limit is 6

}
