package perf;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.CommandLineOptions;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class Perf {

    public static void main(String[] args) throws Exception {
        final Options opt = new OptionsBuilder()
                .parent(new CommandLineOptions(args))
                .shouldFailOnError(true)
                .mode(Mode.Throughput)
                .mode(Mode.SingleShotTime)
                .timeUnit(TimeUnit.SECONDS)
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .threads(32)
                .include(Elastic.class.getName() + ".*")
                .include(Solr.class.getName() + ".*")
                .include(ElasticWrite.class.getName() + ".*")
                .include(SolrWrite.class.getName() + ".*")
                .build();

        new Runner(opt).run();
    }

}
