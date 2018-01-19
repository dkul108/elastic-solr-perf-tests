package perf;

import org.apache.solr.common.SolrDocumentList;
import org.junit.Assert;
import org.junit.Test;

/**
 * ElasticTest
 */
public class SolrTest {

    private <T> void println(T t) {
        System.out.println(t);
    }

    private long notZero(final long value) {
        Assert.assertTrue("Returns zero", value > 0);
        return value;
    }

    private void hits(final SolrDocumentList response) {
        println(notZero(response.getNumFound()));
    }

    private final Solr solr = new Solr();

    @Test
    public void useCase1() throws Exception {
        hits(solr.useCase1());
    }

    @Test
    public void useCase2() throws Exception {
        hits(solr.useCase2());
    }

    @Test
    public void useCase3() throws Exception {
        hits(solr.useCase3());
    }

    @Test
    public void useCase4() throws Exception {
        hits(solr.useCase4());
    }

    @Test
    public void useCase5() throws Exception {
        hits(solr.useCase5());
    }

    @Test
    public void useCase6() throws Exception {
        hits(solr.useCase6());
    }

    @Test
    public void useCase7() throws Exception {
        hits(solr.useCase7());
    }

}