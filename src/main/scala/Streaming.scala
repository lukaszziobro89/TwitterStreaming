import com.typesafe.config.Config
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.Status
import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}


object Streaming extends App{

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val config: Config = ConfigFactory.load()
  val consumerKey: String = config.getString("twitter4j.oauth.consumerKey")
  val consumerSecret: String = config.getString("twitter4j.oauth.consumerSecret")
  val accessToken: String = config.getString("twitter4j.oauth.accessToken")
  val accessTokenSecret: String = config.getString("twitter4j.oauth.accessTokenSecret")

  // Set the system properties so that Twitter4j library used by twitter stream
  // can use them to generate OAuth credentials
//  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
//  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
//  System.setProperty("twitter4j.oauth.accessToken", accessToken)
//  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

  System.setProperty("twitter4j.oauth.consumerKey", "LpCeOO2iYm89WQJopqoBOQK4w")
  System.setProperty("twitter4j.oauth.consumerSecret", "mP7rP4zzHvDSbfHNAWV5r3rItQV7QZxwPwj49hrMYami0BOv4Y")
  System.setProperty("twitter4j.oauth.accessToken", "1944672564-iT5elViH2PmGPEdizwnwIBe9QTNsSyYfcaAlt9U")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "qazBV8bH6B0diTzxUnbg5P7vMpgDlE3xoM7Q2tjxZupqo")

  // To use twitter api you must supply filters
  val filters = "test"
  val sparkConf: SparkConf = new SparkConf()
    .setAppName("TwitterPopularTags")
    .set("spark.executor.memory", "512M")
    .set("spark.executor.cores","2")
    .setMaster("local")
    .set("spark.testing.memory", "2147480000")

  val ssc = new StreamingContext(sparkConf, Seconds(10))
  val stream: ReceiverInputDStream[Status] = TwitterUtils.createStream(ssc, None)

  stream.map(x => x.getText).foreachRDD(s => s.collect())

  // Put your code here
  val hashTags: DStream[String] = stream
    .flatMap(tweet => tweet
      .getText
      .split(" ")
      .filter(_.startsWith("#"))
    )

  val topHashTags: DStream[(String, Int)] = hashTags.map((_,1))
    .reduceByKeyAndWindow(_ + _, Seconds(10))
    .map(_.swap)
      .transform(
        _.sortByKey(ascending = false)
          .zipWithIndex.filter(_._2 < 11).map(_._1)
      )
    .map(_.swap)

  topHashTags.foreachRDD((rdd, ts) => {
    if (!rdd.partitions.isEmpty) {
      logger.info("Popular topics in last 10 seconds (%s total):".format(rdd.count()))
      rdd.foreach {
        case (count, tag) => println("%s (%s tweets)".format(tag, count))
      }
    }
  })

  // Launch the streaming
  ssc.start()
  ssc.awaitTermination()
}
