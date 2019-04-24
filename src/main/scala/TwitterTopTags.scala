import notifications.{SlackUtils, Telegram}
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
  System.setProperty("tweet_mode", "extended")

  val sparkConf = new SparkConf().setAppName("TwitterPopularTags")

  // check Spark configuration for master URL, set it to local if not configured
  if (!sparkConf.contains("spark.master")) {
    sparkConf.setMaster("local[2]").set("spark.testing.memory", "2147480000")
  }

  val ssc = new StreamingContext(sparkConf, Seconds(10))
  val stream = TwitterUtils.createStream(ssc, None)

//  val hashTags = stream
//      .filter(_.getLang == "pl")
//      .flatMap(status => status.getText.split(" "))
//      .filter(_.startsWith("#"))

    val hashTags = stream
      .filter(_.getLang == "en")
      .filter(_.getText.toLowerCase().contains("trump"))
//      .filter(_.getUser.getScreenName.contains(""))
      .map { status =>
        val text = status.getText

//          if(text.toLowerCase().contains("pis")){
//            Telegram.sendNotification(text.toString)
//          }

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
//      println("\nPopular topics in last 10 seconds (%s total):".format(rdd.count()))
      topList.foreach{println}
    })

  val trumpWordCounter = hashTags
    .filter(data => data._1.toLowerCase.contains("trump"))
    .map(data => (data._1, 1))
    .reduceByKeyAndWindow((x,y) => x + y, Seconds(10))
    .map{
      case (trumpWord, count) => (count, trumpWord)
    }

    trumpWordCounter.foreachRDD(rdd => {
      println("------------------------------------------------------------------")
      print("Number of tweets with 'Trump' in tweet in last 10s: ")
        rdd.map{
          tweet => ("Trump", 1)
        }.reduceByKey(_ + _)
          .foreach{
            count => println("%s - %s".format("Trump", count._2.toString))

                  if(count._2 >= 5){
                    Telegram.sendNotification("Trump - " + count._2)
                    SlackUtils.sendMessageToChannel("grafana_luq89", "Trump - " + count._2)
                  }
        }
      println("------------------------------------------------------------------")
    })

  ssc.start()
  ssc.awaitTermination()

}
