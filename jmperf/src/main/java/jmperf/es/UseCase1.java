package jmperf.es;

import jmperf.Elastic;
import jmperf.Sampler;
import org.apache.jmeter.samplers.SampleResult;

/**
 * UseCase1
 */
public class UseCase1 extends Sampler {
    @Override
    protected void runUseCase(final SampleResult result) throws Exception {
        Elastic.elastic.useCase1(result);
    }
}
