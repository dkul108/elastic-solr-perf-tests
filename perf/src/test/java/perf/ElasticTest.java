package perf;

import org.elasticsearch.action.search.SearchResponse;
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
        println(notZero(response.getHits().totalHits));
    }

    private final Elastic elastic = new Elastic();

    @Test
    public void useCase1() throws Exception {
        hits(elastic.useCase1());
    }

    @Test
    public void useCase2() throws Exception {
        hits(elastic.useCase2());
    }

    @Test
    public void useCase3() throws Exception {
        hits(elastic.useCase3());
    }

    @Test
    public void useCase4() throws Exception {
        hits(elastic.useCase4());
    }

    @Test
    public void useCase5() throws Exception {
        hits(elastic.useCase5());
    }

    @Test
    public void useCase6() throws Exception {
        hits(elastic.useCase6());
    }

    @Test
    public void useCase7() throws Exception {
        hits(elastic.useCase7());
    }

}