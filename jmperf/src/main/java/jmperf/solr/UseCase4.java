package jmperf.solr;

import jmperf.Sampler;
import jmperf.Solr;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase4
 */
public class UseCase4 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Solr.solr.useCase4(result);
    }
}
