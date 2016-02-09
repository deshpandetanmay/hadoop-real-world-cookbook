package com.demo

import org.apache.spark._
import org.apache.spark.sql._

object JsonSpark {
 

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("SPARKJSON")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val users = sqlContext.read.json("/json/people.json")
  
    users.map(t => "Name: " + t(0)).collect().foreach(println)
    users.show()

		// Print the schema in a tree format
		users.printSchema()

		// Select only the "name" column
		users.select("name").show()

		// Select everybody, but increment the age by 1
		users.select(users("name"), users("age") + 1).show()

		// Select people older than 21
		users.filter(users("age") > 21).show()

		// Count people by age
		users.groupBy("age").count().show()

    sc.stop()
  }
}
