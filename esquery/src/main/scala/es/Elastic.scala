package es

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
class Elastic {

  import UseCases._

  client.listedNodes()

  @Benchmark
  def useCase1: Unit = check(UseCase1)

  @Benchmark
  def useCase1_2: Unit = check(UseCase1_2)

  @Benchmark
  def useCase2: Unit = check(UseCase2)

  @Benchmark
  def useCase3: Unit = check(UseCase3)

  @Benchmark
  def useCase4: Unit = check(UseCase4)

  @Benchmark
  def useCase5: Unit = check(UseCase5)

}
