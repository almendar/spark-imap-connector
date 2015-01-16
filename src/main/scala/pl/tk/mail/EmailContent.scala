package pl.tk.mail

/**
 * Created by tomaszk on 1/16/15.
 */
case class EmailContent(content:String,msgType:MimeType) {
  override def toString() : String = {
    s"EmailContent(${content} (...),$msgType})"
  }
}
