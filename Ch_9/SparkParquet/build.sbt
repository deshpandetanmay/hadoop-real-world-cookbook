name := "SparkParquet"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(

 "com.google.guava" % "guava" % "14.0",
 "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
 "org.apache.spark" %% "spark-sql"  % "1.6.0" % "provided"
)

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"