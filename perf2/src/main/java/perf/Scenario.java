package perf;

import model.Guide;
import org.openjdk.jmh.annotations.Benchmark;

import static perf.Common.*;

public abstract class Scenario<Query, Result> {

    public abstract Result search(boolean cacheHint, Query query) throws Exception;

    public Result search(final Query query) throws Exception {
        return search(true, query);
    }

    /**
     * given a channel Id and a starttime and endtime<br/> return the airings between those times in order of air-times<br/> execute the above query with
     * multiple channelIds
     */
    public abstract Query useCase1(long start, long end, String... channels);

    @Benchmark
    public Result useCase1() throws Exception {
        return search(false, useCase1(now(), futureDays(1), randomChannels()));
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times execute the above query with multiple channelIds
     */
    public abstract Query useCase2(int count, long now, String... channels);

    @Benchmark
    public Result useCase2() throws Exception {
        return search(useCase2(randomCount(), randomNow(), randomChannels()));
    }

    /**
     * given a filter query, return the airing and channel which matches. eg. If I say the filter is on the Category field and the value is Movie, return all
     * the airings on now which have the category movie, sorted by channel number.
     */

    public abstract Query useCase3(long now, String category);

    @Benchmark
    public Result useCase3() throws Exception {
        return search(useCase3(randomNow(), category));
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    public abstract Query useCase4(String... channels);

    @Benchmark
    public Result useCase4() throws Exception {
        return search(useCase4(randomChannel()));
    }

    /**
     * for any of the above queries, filter the results by any given restriction on the packages array. eg. by a Package Name (Europe)
     */
    public abstract Query useCase5(String packageName, Query query);

    @Benchmark
    public Result useCase5() throws Exception {
        return search(useCase5(packageName, useCase4(randomChannel())));
    }

    /**
     * by a country (the airing should have a package with a country array having a given country)
     */
    public abstract Query useCase6(String country, Query query);

    @Benchmark
    public Result useCase6() throws Exception {
        return search(useCase6(country, useCase4(randomChannel())));
    }

    /* by a concurrency limit */
    public abstract Query useCase7(int limit, Query query);

    @Benchmark
    public Result useCase7() throws Exception {
        return search(useCase7(2, useCase4(randomChannel())));
    }

    public abstract void insert(Guide g) throws Exception;

    @Benchmark
    public void write() throws Exception {
        insert(randomGuide());
    }

}
