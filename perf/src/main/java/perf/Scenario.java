package perf;

import static perf.Common.*;

public interface Scenario<Query, Result> {

    Result search(boolean cacheHint, Query query) throws Exception;

    default Result search(final Query query) throws Exception {
        return search(true, query);
    }

    /**
     * given a channel Id and a starttime and endtime<br/>
     * return the airings between those times in order of air-times<br/>
     * execute the above query with multiple channelIds
     */
    Query useCase1(long start, long end, String... channels);

    default Result runUseCase1() throws Exception {
        return search(false, useCase1(now(), futureDays(1), randomChannels()));
    }

    /**
     * ﻿given a channelId return the current airing next n airings in order of air-times
     * execute the above query with multiple channelIds
     */
    Query useCase2(int count, long now, String... channels);

    default Result runUseCase2() throws Exception {
        return search(false, useCase2(randomCount(), randomNow(), randomChannels()));
    }

    /**
     * given a filter query, return the airing and channel which matches.
     * eg. If I say the filter is on the Category field and the value is Movie,
     * return all the airings on now which have the category movie, sorted by channel number.
     */

    Query useCase3(long now, String category);

    default Result runUseCase3() throws Exception {
        return search(useCase3(randomNow(), category));
    }

    /**
     * given a channelId return the complete set of airings up to 3 days prior and 14 days in the future
     */
    Query useCase4(String... channels);

    default Result runUseCase4() throws Exception {
        return search(useCase4(randomChannel()));
    }

    /**
     * for any of the above queries, filter the results by any given restriction on the packages array.
     * eg. by a Package Name (Europe)
     */
    Query useCase5(String packageName, Query query);

    default Result runUseCase5() throws Exception {
        return search(useCase5(packageName, useCase4(randomChannel())));
    }

    /**
     * by a country (the airing should have a package with a country array having a given country)
     */
    Query useCase6(String country, Query query);

    default Result runUseCase6() throws Exception {
        return search(useCase6(country, useCase4(randomChannel())));
    }

    /* by a concurrency limit */
    Query useCase7(int limit, Query query);

    default Result runUseCase7() throws Exception {
        return search(useCase7(2, useCase4(randomChannel())));
    }

}
