package jmperf.solr;

import jmperf.Sampler;
import jmperf.Solr;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase3
 */
public class UseCase3 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Solr.solr.useCase3(result);
    }
}
