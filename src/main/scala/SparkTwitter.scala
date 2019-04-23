import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import twitter4j.FilterQuery
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder


object SparkTwitter extends App{

    val appName = "TwitterData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]").set("spark.testing.memory", "2147480000")
    val ssc = new StreamingContext(conf, Seconds(5))

    val consumerKey = "LpCeOO2iYm89WQJopqoBOQK4w"
    val consumerSecret = "mP7rP4zzHvDSbfHNAWV5r3rItQV7QZxwPwj49hrMYami0BOv4Y"
    val accessToken = "1944672564-iT5elViH2PmGPEdizwnwIBe9QTNsSyYfcaAlt9U"
    val accessTokenSecret = "qazBV8bH6B0diTzxUnbg5P7vMpgDlE3xoM7Q2tjxZupqo"

    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)

    val auth = new OAuthAuthorization(cb.build)
    val tweets = TwitterUtils.createStream(ssc, Some(auth))

  println(tweets
    .map((_, 1))
  )

    println(tweets.flatMap(x => x.getText).count())

    val s = tweets.foreachRDD(x => x
        .filter(tweet => tweet.getUser.getId.equals(1944672564))
        .map(tweet => tweet.getText)
        .collect()
        .foreach(a => println(a))
    )

//    val luq89_all = tweets
//        .flatMap(status => status.getText)

//    luq89_all.print()

//    val hashTags = tweets
//      .map(status => status.getUser.getName)

//    hashTags.print()

//    val hashTags = tweets
//      .flatMap(status => status
//        .getText
//        .split(" ")
//        .filter(_.startsWith("#")))
//
//    (1344951,5988062,807095,3108351)



//    tweets --> CLASS: class org.apache.spark.streaming.twitter.TwitterInputDStream
    ssc.start()
    ssc.awaitTermination()
}

