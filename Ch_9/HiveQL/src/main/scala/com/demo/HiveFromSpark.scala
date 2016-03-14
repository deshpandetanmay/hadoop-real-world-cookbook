package com.demo

import com.google.common.io.ByteStreams
import com.google.common.io.Files


import java.io.File

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.hive.HiveContext

object HiveFromSpark {
  case class Record(id: Int, name: String)

  // Copy emp.txt file from classpath to temporary directory
  val empStream = HiveFromSpark.getClass.getResourceAsStream("/emp.txt")
  val empFile = File.createTempFile("emp", "txt")
  empFile.deleteOnExit()
  ByteStreams.copy(empStream, Files.newOutputStreamSupplier(empFile))

  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HiveFromSpark")
    val sc = new SparkContext(sparkConf)

 
    val hiveContext = new HiveContext(sc)
    import hiveContext.implicits._
    import hiveContext.sql

    sql("CREATE TABLE IF NOT EXISTS empSpark (id INT, name STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','")
    sql(s"LOAD DATA LOCAL INPATH '${empFile.getAbsolutePath}' INTO TABLE empSpark")

    // Queries are expressed in HiveQL
    println("Result of 'SELECT *': ")
    sql("SELECT * FROM empSpark").collect().foreach(println)

    // Aggregation queries are also supported.
    val count = sql("SELECT COUNT(*) FROM empSpark").collect().head.getLong(0)
    println(s"COUNT(*): $count")

    // The results of SQL queries are themselves RDDs and support all normal RDD functions.  The
    // items in the RDD are of type Row, which allows you to access each column by ordinal.
    val rddFromSql = sql("SELECT id, name FROM empSpark WHERE id < 20 ORDER BY id")

    println("Result of RDD.map:")
    val rddAsStrings = rddFromSql.map {
      case Row(id: Int, name: String) => s"Key: $id, Value: $name"
    }

    // You can also register RDDs as temporary tables within a HiveContext.
    val rdd = sc.parallelize((1 to 100).map(i => Record(i, s"val_$i")))
    rdd.toDF().registerTempTable("records")

    // Queries can then join RDD data with data stored in Hive.
    println("Result of SELECT *:")
    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").collect().foreach(println)

    sc.stop()
  }
}
