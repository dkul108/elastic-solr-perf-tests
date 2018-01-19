
scalaVersion := "2.12.4"

fork := true

enablePlugins(JmhPlugin)

libraryDependencies += "org.elasticsearch.client" % "transport" % "5.6.3"
