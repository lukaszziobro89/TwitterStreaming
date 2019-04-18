import twitter4j.TwitterStreamFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import twitter4j.FilterQuery

object StatusStreamer{
  def main(args: Array[String]) {

    val appName = "TwitterData"
//    val conf = new SparkConf()
//    conf.setAppName(appName).setMaster("local[2]").set("spark.testing.memory", "2147480000")
//    val ssc = new StreamingContext(conf, Seconds(5))

    val twitterStream = new TwitterStreamFactory(Util.config).getInstance
    twitterStream.addListener(Util.simpleStatusListener)
//    twitterStream.sample()
//    twitterStream.filter(new FilterQuery().language("pl"))
//    twitterStream.sample("pl")

    val tweets: Unit = twitterStream
      .filter(new FilterQuery()
                    .language("pl")
                    .track("pis")
              )

//    printToFile(tweets.toString)
//
//    def printToFile(content: String,
//                    location: String = "C:\\Users\\ah0182322\\IdeaProjects\\dependencyTest\\src\\main\\resources\\tweets.txt"): Unit =
//      Some(new java.io.PrintWriter(location)).foreach{f => try{f.write(content)}finally{f.close()}}

//    Array(1344951,5988062,807095,3108351)))
//    Thread.sleep(60000)
//        twitterStream.cleanUp()
//    twitterStream.shutdown()

//    ssc.start()
//    ssc.awaitTermination()


  }
}

