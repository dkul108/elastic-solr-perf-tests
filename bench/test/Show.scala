package bench

import org.junit.Test

class Show {

  def show[T](n: String)(t: T) = println(
    "-----------------------------------------\n" +
      n + " : " + t.toString + " found"
      + "\n-----------------------------------------"
  )

  @Test
  def elst1() = show("Elastic 1") {
    new Elst().useCase1.getHits.totalHits
  }

  @Test
  def elst2() = show("Elastic 2") {
    new Elst().useCase2.getHits.totalHits
  }

  @Test
  def elst4() = show("Elastic 4") {
    new Elst().useCase4.getHits.totalHits
  }

  @Test
  def solr1() = show("Solr 1") {
    new Solr().useCase1.getNumFound
  }

  @Test
  def solr2() = show("Solr 2") {
    new Solr().useCase2.getNumFound
  }

  @Test
  def solr4() = show("Solr 4") {
    new Solr().useCase4.getNumFound
  }

}
