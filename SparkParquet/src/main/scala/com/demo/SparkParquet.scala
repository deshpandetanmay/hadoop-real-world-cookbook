package com.demo


import org.apache.spark._
import org.apache.spark.sql._

object SparkParquet {
 

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("SparkParquet")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val users = sqlContext.read.load("/parquet/users.parquet")
   
    users.select("name", "favorite_color").write.save("/parquet/namesAndFavColors.parquet")
    
    users.registerTempTable("userParquetFile")
    users.map(t => "Name: " + t(0)).collect().foreach(println)
    sc.stop()
  }
}
