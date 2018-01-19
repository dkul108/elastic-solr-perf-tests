package perf;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
public class ElasticWrite extends Elastic {

    private final Common.Generator generator = Common.generator(this);

    @Setup
    public void setup() {
        generator.start();
    }

    @TearDown
    public void tearDown() throws InterruptedException {
        generator.stop();
    }
}
