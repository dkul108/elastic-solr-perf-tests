package perf;

import org.apache.solr.client.solrj.SolrQuery;
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
        println("[Returns " + notZero(response.getNumFound()) + " documents]\n");
    }

    private final Solr solr = new Solr() {
        @Override
        public SolrDocumentList search(final boolean cacheHint,
                final SolrQuery query) throws Exception {
            println(query);
            return super.search(cacheHint, query);
        }
    };

    @Test
    public void useCase1() throws Exception {
        println("useCase1");
        hits(solr.useCase1());
    }

    @Test
    public void useCase2() throws Exception {
        println("useCase2");
        hits(solr.useCase2());
    }

    @Test
    public void useCase3() throws Exception {
        println("useCase3");
        hits(solr.useCase3());
    }

    @Test
    public void useCase4() throws Exception {
        println("useCase4");
        hits(solr.useCase4());
    }

    @Test
    public void useCase5() throws Exception {
        println("useCase5");
        hits(solr.useCase5());
    }

    @Test
    public void useCase6() throws Exception {
        println("useCase6");
        hits(solr.useCase6());
    }

    @Test
    public void useCase7() throws Exception {
        println("useCase7");
        hits(solr.useCase7());
    }

}