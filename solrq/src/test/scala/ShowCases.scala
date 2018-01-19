package solrq

import org.apache.solr.common.params.SolrParams
import org.junit.Test
import solrq.UseCases._

class ShowCases {

  def show(name: String, q: SolrParams) =
    println(name + ": " + guide(q))

  @Test
  def testCase1() = show("UseCase1", UseCase1)

  @Test
  def testCase2() = show("UseCase2", UseCase2)

  @Test
  def testCase4() = show("UseCase4", UseCase4)
}
