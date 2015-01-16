package pl.tk.spark.streaming.imap

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import pl.tk.mail.Email

/**
 * Created by tomaszk on 1/15/15.
 */
object ImapMain extends App{
  val sparkConf = new SparkConf().setAppName("ImapMails").setMaster("local[2]")
  val ssc = new StreamingContext(sparkConf, Seconds(2))
  /**
   * Here you can set name of the folder to stream email from.
   * For gmail "inbox" should be fine.
   * But for best observations create an empty directory e.g. Testing and copy e-mail by hand there.
   */
  val stream: ReceiverInputDStream[Email] = ImapUtils.createStream(ssc,"Testing")
  val subjects: DStream[String] = stream.map(_.subject)
  subjects.foreachRDD {rdd =>
    val entries = rdd.collect()
    if(entries.nonEmpty)
      println(entries.mkString("\n"))
  }
  ssc.start()
  ssc.awaitTermination()

}
