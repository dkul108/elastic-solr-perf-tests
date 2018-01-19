import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.{Row, SparkSession}
import org.elasticsearch.spark.sql._

object EsLoad extends App {

  args match {
    case Array(src, dst) => time(s"ES load $src -> $dst", loadData(src, dst))
    case _ => println("usage: src dst")
  }

  def time[T](msg: String, f: => T): T = {
    val start = System.currentTimeMillis()
    val t = f
    println(s"$msg  ${
      (System.currentTimeMillis() - start) / 1000
    } seconds")
    t
  }

  lazy val spark = SparkSession.builder()
    .appName("EsLoad").master("local[*]")
    .config("spark.executor.memory", "2G")
    .config("spark.driver.memory", "2G")
    .config("es.index.auto.create", "true")
    //    .config("es.nodes.discovery", "false")
    //    .config("es.nodes.wan.only", "true")
    .getOrCreate()

  import spark.implicits._

  def fixDate(row: Row, field: String): Row = {
    if (row.getAs[String](field) == "")
      Row.fromSeq(row.toSeq.updated(row.fieldIndex(field), null))
    else row
  }

  lazy val originalAirDate = "originalAirDate"

  def loadData(src: String, dst: String) = {
    val data = spark.read.json(src)

    val schema = data.schema
    implicit val encoder = RowEncoder(schema)

    (if (schema.fieldNames.contains(originalAirDate)) {
      println(originalAirDate + " on")
      data.map(row => fixDate(row, originalAirDate))
    } else {
      println(originalAirDate + " off")
      println("")
      data
    })
      //.show()
      .saveToEs(dst)
  }

}