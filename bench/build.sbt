scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := baseDirectory.value / "test"

scalaVersion := "2.12.4"

fork := true

enablePlugins(JmhPlugin)

libraryDependencies += "org.elasticsearch.client" % "transport" % "5.6.3"
libraryDependencies += "org.apache.solr" % "solr-solrj" % "7.1.0"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
