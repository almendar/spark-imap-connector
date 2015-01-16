package pl.tk.spark.streaming.imap

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import pl.tk.mail.Email

/**
 * Created by tomaszk on 1/15/15.
 */
object ImapUtils {
  def createStream(
                    ssc: StreamingContext,
                    folderName:String,
                    storageLevel: StorageLevel = StorageLevel.MEMORY_ONLY
                    ): ReceiverInputDStream[Email] = {
    new ImapInputDStream(ssc,folderName)
  }
}
