package jmperf;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;

public abstract class Sampler extends AbstractJavaSamplerClient implements Serializable {

    protected abstract void runUseCase(final SampleResult result) throws Exception;

    @Override
    public SampleResult runTest(final JavaSamplerContext context) {
        final SampleResult result = new SampleResult();
        try {
            runUseCase(result);
            result.setSuccessful(true);
            result.setResponseMessage("OK");
        } catch (final Exception e) {
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            if (result.getEndTime() == 0L)
                result.sampleEnd();
        }
        return result;
    }
}
