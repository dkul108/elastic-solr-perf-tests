package jmperf.es;

import jmperf.Elastic;
import jmperf.Sampler;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase3
 */
public class UseCase3 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Elastic.elastic.useCase3(result);
    }
}
