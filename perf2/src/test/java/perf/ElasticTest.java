package perf;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * ElasticTest
 */
public class ElasticTest {

    private <T> void println(T t) {
        System.out.println(t);
    }

    private long notZero(final long value) {
        Assert.assertTrue("Returns zero", value > 0);
        return value;
    }

    private void hits(final SearchResponse response) {
        println("[Returns " + notZero(response.getHits().totalHits) + " documents]\n");
    }

    private final Elastic elastic = new Elastic() {
        @Override
        public SearchResponse search(final boolean cache,
                final SearchSourceBuilder query) {
            println(query);
            return super.search(cache, query);
        }
    };

    @Test
    public void useCase1() throws Exception {
        println("useCase1");
        hits(elastic.useCase1());
    }

    @Test
    public void useCase2() throws Exception {
        println("useCase2");
        hits(elastic.useCase2());
    }

    @Test
    public void useCase3() throws Exception {
        println("useCase3");
        hits(elastic.useCase3());
    }

    @Test
    public void useCase4() throws Exception {
        println("useCase4");
        hits(elastic.useCase4());
    }

    @Test
    public void useCase5() throws Exception {
        println("useCase5");
        hits(elastic.useCase5());
    }

    @Test
    public void useCase6() throws Exception {
        println("useCase6");
        hits(elastic.useCase6());
    }

    @Test
    public void useCase7() throws Exception {
        println("useCase7");
        hits(elastic.useCase7());
    }

}