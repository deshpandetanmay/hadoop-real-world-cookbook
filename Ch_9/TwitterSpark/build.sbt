name := "TwitterSpark"

version := "1.0"

scalaVersion := "2.11.7"

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}

libraryDependencies ++= Seq(

 "com.google.guava" % "guava" % "14.0",
 "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
 "org.apache.spark" %% "spark-streaming" % "1.6.0" % "provided",
 "org.apache.spark" %% "spark-streaming-twitter" % "1.6.0"
)

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"