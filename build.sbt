name := "spark-imap-connector"

version := "0.0.1"

scalaVersion := "2.10.4"

scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8", "-Xlint")


unmanagedResourceDirectories in Compile += baseDirectory.value / "conf"