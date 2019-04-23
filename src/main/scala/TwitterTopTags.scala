import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TwitterTopTags extends App{

  // Set logging level if log4j not configured (override by adding log4j.properties to classpath)
  if (!Logger.getRootLogger.getAllAppenders.hasMoreElements) {
    Logger.getRootLogger.setLevel(Level.WARN)
  }

  val consumerKey = "LpCeOO2iYm89WQJopqoBOQK4w"
  val consumerSecret = "mP7rP4zzHvDSbfHNAWV5r3rItQV7QZxwPwj49hrMYami0BOv4Y"
  val accessToken = "1944672564-iT5elViH2PmGPEdizwnwIBe9QTNsSyYfcaAlt9U"
  val accessTokenSecret = "qazBV8bH6B0diTzxUnbg5P7vMpgDlE3xoM7Q2tjxZupqo"
  
  // Set the system properties so that Twitter4j library used by twitter stream
  // can use them to generate OAuth credentials
  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

  val sparkConf = new SparkConf().setAppName("TwitterPopularTags")

  // check Spark configuration for master URL, set it to local if not configured
  if (!sparkConf.contains("spark.master")) {
    sparkConf.setMaster("local[2]")
  }

  val ssc = new StreamingContext(sparkConf, Seconds(5))
  val stream = TwitterUtils.createStream(ssc, None)

//  val hashTags = stream
//      .filter(_.getLang == "pl")
//      .flatMap(status => status.getText.split(" "))
//      .filter(_.startsWith("#"))

    val hashTags = stream
//      .filter(_.getLang == "pl")
//      .filter(_.getUser.getScreenName.contains(""))
      .map { status =>
        val text = status.getText
        val user = status.getUser.getName
        val screenName = status.getUser.getScreenName
        val date = status.getCreatedAt.toString
        (text, user, screenName, date)
    }



//  val hashTags = stream
//    .filter(_.getLang == "pl")
//    .filter(_.getUser.getScreenName == "luq89")
//      .filter(_.getUser.getStatus.getUser.getScreenName == "luq89")
//    .filter(tweet => tweet.getUser.getStatus.getUser.getScreenName == "luq89")
//    .filter(_.getLang == "pl")
//    .filter(_.getLang == "und")
//    .map(status => status.getText)
//      .map(status => status.getUser.getScreenName)
//      .filter(_.contains("lu"))
//      .filter(_.contains("pis"))

    hashTags.foreachRDD(rdd => {
      val topList = rdd
      println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
      topList.foreach{println}
    })

  /**
  val topCounts60 = hashTags
    .map((_, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(60))
    .map{case (topic, count) => (count, topic)}
    .transform(_.sortByKey(ascending = false))

  val topCounts10 = hashTags
    .map((_, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(10))
    .map{case (topic, count) => (count, topic)}
    .transform(_.sortByKey(ascending = false))


  // Print popular hashtags
  topCounts60.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
    topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
  })

  topCounts10.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
    topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
  })
*/
  ssc.start()
  ssc.awaitTermination()

}
