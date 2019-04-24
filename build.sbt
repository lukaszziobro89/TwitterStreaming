name := "TwitterStreaming"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "com.google.code.gson" % "gson" % "2.7"
libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "3.0.3"
libraryDependencies += "org.twitter4j" % "twitter4j-core" % "3.0.3"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.2"
libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.6.3"
libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.8"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.6"
