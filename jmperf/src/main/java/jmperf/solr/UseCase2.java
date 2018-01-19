package jmperf.solr;

import jmperf.Sampler;
import jmperf.Solr;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase2
 */
public class UseCase2 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Solr.solr.useCase2(result);
    }
}
