import twitter4j._

object Util {
  val config = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey("LpCeOO2iYm89WQJopqoBOQK4w")
    .setOAuthConsumerSecret("mP7rP4zzHvDSbfHNAWV5r3rItQV7QZxwPwj49hrMYami0BOv4Y")
    .setOAuthAccessToken("1944672564-iT5elViH2PmGPEdizwnwIBe9QTNsSyYfcaAlt9U")
    .setOAuthAccessTokenSecret("qazBV8bH6B0diTzxUnbg5P7vMpgDlE3xoM7Q2tjxZupqo")
    .build

  def simpleStatusListener = new StatusListener() {
    def onStatus(status: Status) { println(status.getText) }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }
}




