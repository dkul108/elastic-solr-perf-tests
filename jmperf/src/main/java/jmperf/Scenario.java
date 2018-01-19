package jmperf;

import org.apache.jmeter.samplers.SampleResult;

import static jmperf.Common.*;

public abstract class Scenario<Query, Result> {

    public abstract Result search(final SampleResult result,
                                  final boolean cacheHint,
                                  final Query query) throws Exception;

    public Result search(final SampleResult result,
                         final Query query) throws Exception {
        return search(result, true, query);
    }

    /**
     * given a channel Id and a starttime and endtime<br/> return the airings between those times in order of air-times<br/> execute the above query with
     * multiple channelIds
     */
    public abstract Query useCase1(long start, long end, String... channels);

    public Result useCase1(final SampleResult result) throws Exception {
        return search(result, false,
                useCase1(now(), futureDays(1), randomChannels()));
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times execute the above query with multiple channelIds
     */
    public abstract Query useCase2(int count, long now, String... channels);

    public Result useCase2(final SampleResult result) throws Exception {
        return search(result, useCase2(randomCount(), randomNow(), randomChannels()));
    }

    /**
     * given a filter query, return the airing and channel which matches. eg. If I say the filter is on the Category field and the value is Movie, return all
     * the airings on now which have the category movie, sorted by channel number.
     */

    public abstract Query useCase3(long now, String category);

    public Result useCase3(final SampleResult result) throws Exception {
        return search(result, useCase3(randomNow(), category));
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    public abstract Query useCase4(String... channels);

    public Result useCase4(final SampleResult result) throws Exception {
        return search(result, useCase4(randomChannel()));
    }

}
