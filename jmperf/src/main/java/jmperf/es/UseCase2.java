package jmperf.es;

import jmperf.Elastic;
import jmperf.Sampler;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase2
 */
public class UseCase2 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Elastic.elastic.useCase2(result);
    }
}
