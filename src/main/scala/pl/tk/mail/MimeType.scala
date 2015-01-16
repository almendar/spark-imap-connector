package pl.tk.mail

sealed trait MimeType
case object `TEXT/PLAIN` extends MimeType
case object `TEXT/HTML` extends MimeType