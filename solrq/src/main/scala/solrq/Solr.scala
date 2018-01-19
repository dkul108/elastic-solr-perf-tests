package solrq

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class Solr {

  import UseCases._

  @Benchmark
  def useCase1 = guide(UseCase1)

  @Benchmark
  def useCase2 = guide(UseCase2)

  @Benchmark
  def useCase4 = guide(UseCase4)

}
