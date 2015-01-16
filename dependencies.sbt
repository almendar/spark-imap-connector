val Spark_V    = "1.2.0"

libraryDependencies ++= Seq("org.apache.spark"  %% "spark-core" % Spark_V,
  "org.apache.spark"  %% "spark-streaming" % Spark_V,
  "org.apache.spark"  %% "spark-sql"       % Spark_V)

libraryDependencies += "javax.mail" % "mail" % "1.4.7"