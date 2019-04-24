import twitter4j._

object UserTweets extends App{


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


  val twitter = new TwitterFactory().getInstance
  val twitterStream = new TwitterStreamFactory().getInstance()
  var statuses = twitter.getUserTimeline(1944672564)

  twitterStream.addListener(Util.simpleStatusListener)

//  twitterStream.filter()
  twitterStream.sample()

//  println(statuses)

//  val iterator = statuses.iterator()
//  while (iterator.hasNext){
//    println(iterator.next().getText)
//  }

}
