package pl.tk.spark.streaming.imap

import java.io.FileInputStream
import java.util.Properties
import javax.mail._

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.receiver.Receiver
import pl.tk.mail.imap.GmailInbox
import pl.tk.mail.{`TEXT/PLAIN`, `TEXT/HTML`, EmailContent, Email}

/**
 * Created by tomaszk on 1/15/15.
 */
private [imap] class  ImapInputDStream(ssc:StreamingContext,folderName : String,storageLevel: StorageLevel = StorageLevel.MEMORY_ONLY) extends ReceiverInputDStream[Email](ssc) {
  override def getReceiver(): Receiver[Email] = new ImapReceiver(folderName,storageLevel)
}

private [imap] class ImapReceiver(folderName:String,storageLevel: StorageLevel) extends Receiver[Email](storageLevel) {

  var readEmails : Int = 0;
  val me = this
  var t : Thread = null;

  val p = new Properties();
  p.load(new FileInputStream("conf/imap.properties"));
  val gmail = new GmailInbox(p.getProperty("gmail.user"),p.getProperty("gmail.password"));

  override def onStart(): Unit = {
    val t  = new Thread(new Runnable {
      override def run(): Unit = {
       while (true) {
         val messages = gmail.getMessages(folderName,10,readEmails)
         readEmails += messages.length
         messages foreach store
         Thread.sleep(2000);
       }
      }
    });
    t.run()
  }

  override def onStop(): Unit = {
    t.interrupt()
  }
}