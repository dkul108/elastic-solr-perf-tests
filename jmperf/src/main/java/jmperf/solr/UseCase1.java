package jmperf.solr;

import jmperf.Sampler;
import jmperf.Solr;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase1
 */
public class UseCase1 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Solr.solr.useCase1(result);
    }
}
