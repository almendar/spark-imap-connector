package pl.tk.mail.imap
import java.io.FileInputStream
import java.util.Properties
import javax.mail._
import javax.mail.event.{ConnectionEvent, ConnectionListener, StoreEvent, StoreListener}
import pl.tk.mail.{`TEXT/PLAIN`, `TEXT/HTML`, EmailContent, Email}
import scala.collection.immutable.Seq

class GmailInbox(user:String,password:String) extends Serializable {
  val props = new Properties()
  props.setProperty("mail.imap.host","smtp.gmail.com")
  props.setProperty("mail.imap.socketFactory.port","993")
  props.setProperty("mmail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory")
  props.setProperty("mmail.imap.auth","true")
  props.setProperty("mmail.imap.port","993")


  private def getConnectedStore()  :Store =  {
    val session: Session = Session.getDefaultInstance(props, null)
    val store: Store = session.getStore("imaps");
    store.connect("smtp.gmail.com", user, password);
    store
  }


  def listDefaultFolder : Seq[String] = {
    val store= getConnectedStore()
    val ret = store.getDefaultFolder.list().toList.map(_.getName)
    store.close();
    ret
  }

  def listFolder(folderName:String) : Seq[String] = {
    val store= getConnectedStore()
   val ret =  store.getFolder(folderName).list().toList.map(_.getName)
    store.close()
    ret
  }



  def getMessages(folder:String="inbox",take:Int = Int.MaxValue, drop:Int = 0) : Seq[Email] = {
    val store = getConnectedStore()
    val inbox = store.getFolder(folder)
    inbox.open(Folder.READ_ONLY)
    val ret = inbox.getMessages.toStream.map{ (x:Message) =>
      val from: String = x.getFrom match {
        case null => "Null"
        case Array() => "Empty"
        case all @ Array(_*) => all.mkString(",")
        case _ => "Unknown"
      }
      if(x.getContent.isInstanceOf[Multipart]) {
        val multipart : Multipart = x.getContent.asInstanceOf[Multipart]
        val p: Seq[EmailContent] = (0 until multipart.getCount) flatMap { bodyIndex =>
          val bodyPart : BodyPart = multipart.getBodyPart(bodyIndex)
          if(bodyPart.getContentType.contains(`TEXT/HTML`.toString))
            Some(EmailContent(bodyPart.getContent.asInstanceOf[String],`TEXT/HTML`))
          else if(bodyPart.getContentType.contains(`TEXT/PLAIN`.toString))
            Some(EmailContent(bodyPart.getContent.asInstanceOf[String],`TEXT/PLAIN`))
          else None
        }
        Email(from,x.getSubject,p)
      }
      else {
        val mimeType = if(x.getContentType.contains(`TEXT/HTML`.toString)) `TEXT/HTML` else `TEXT/PLAIN`
        Email(from,x.getSubject,Seq(EmailContent(x.getContent.asInstanceOf[String],mimeType)))
      }
    }
    .drop(drop)
    .take(take)
    .toList
    store.close()
    ret
  }
}

