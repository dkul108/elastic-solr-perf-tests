package com.att.snr;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.wavesoftware.jmh.junit.utilities.JmhCleaner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.att.snr.EsQueriesUtils.airingsTimeRange;
import static com.att.snr.EsQueriesUtils.channels;
import static com.att.snr.EsQueriesUtils.checkOkStatus;
import static com.sun.org.glassfish.external.statistics.impl.StatisticImpl.START_TIME;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

//ssh -L 9200:localhost:9200 ubuntu@18.216.156.123
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EsQueriesRestIT {

    @Autowired
    private RestHighLevelClient client;

    @ClassRule
    public static JmhCleaner cleaner = new JmhCleaner(EsQueriesIT.class);

    private static Header[] HEADERS = {
                new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader("Role", "Read")
    };


    private static final Collection<String> channels = Lists.newArrayList("CHAN_45", "CHAN_46");
    private static final String START_TIME = "2017-11-05T10:00:50";
    private static final String END_TIME = "2017-11-05T12:00:00";

    /**
     * given a channel Id and a START_TIME time and END_TIME time
     * return the airings between those times in order of air-times
     * execute the above query with multiple channelIds
     */
    @Test
    public void whenFindAiringsByChannelIdAndStartEnd_thenReturnAiringsBetweenStartEndInOrderAirTimes() throws IOException {
        boolean ping = client.ping(HEADERS);
        assertThat(ping).isTrue();

        final BoolQueryBuilder filter =
                boolQuery().
                    filter(termsQuery("channelId.keyword", channels)).
                    filter(rangeQuery("start").from(START_TIME)).
                    filter(rangeQuery("end").to(END_TIME));

        SearchRequest searchRequest = new SearchRequest().
                                            //indices("guide").
                                            types("guide").
                                            source(
                                                    new SearchSourceBuilder().
                                                            query(filter).
                                                            sort("start", SortOrder.ASC)
                                            );

        client.search(searchRequest, HEADERS).getHits().getHits().toString();
    }
}
