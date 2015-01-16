# spark-imap-connector
Warsaw Apache Spark mini-hackaton outcome. Event was held on 2015.01.15.

This is a very simple sample how to create your own streams to spark. 

Example is tightly integrated with gmail so for testing checkout `conf/imap.properties` file where you can pass your own credentials. 

Additionally you might want to change the imap folder to stream from. Edit code in `object ImapMain`. For gmail passing `inbox` should be fine.

For best effect create an empty folder and copy emails to it and look at the output.
