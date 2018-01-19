name := "EsLoad"

version := "0.1"

scalaVersion := "2.11.8"

fork in run := true

javaOptions in run += "-Xmx4g"

libraryDependencies := Seq (
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.elasticsearch" %% "elasticsearch-spark-20" % "5.6.3"
)
