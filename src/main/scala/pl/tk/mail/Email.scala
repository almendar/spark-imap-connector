package pl.tk.mail

case class Email(from:String,subject:String,content:Seq[EmailContent])