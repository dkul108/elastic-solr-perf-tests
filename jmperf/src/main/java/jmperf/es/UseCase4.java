package jmperf.es;

import jmperf.Elastic;
import jmperf.Sampler;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase4
 */
public class UseCase4 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Elastic.elastic.useCase4(result);
    }
}
