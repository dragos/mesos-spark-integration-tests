val sparkHomeArg = sys.props.get("spark.home")

val sparkHome = SettingKey[String]("spark-home", "the value of the variable 'spark.home'")
val sparkMaster = SettingKey[String]("spark-master", "the value of the variable 'spark.master'")
val sparkExecutorURI = SettingKey[String]("spark-executor-uri", "the value of the variable 'spark.executor.uri', location of the spark jar on the cluster")

lazy val root = (project in file(".")).
  settings(
    organization := "com.typesafe.spark",
    name := "mesos-spark-integration-tests",
    version := "0.1.0",
    scalaVersion := "2.10.5",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.4"
    ),
    sparkHome := {
        sys.props.get("spark.home").getOrElse(error("spark.home not defined. Use '-Dspark.home=...'"))
    },
    sparkMaster := {
        // TODO: check if spark.master is a mesos URI (or zookeeper)
        sys.props.get("spark.master").getOrElse(error("spark.master not defined. Use '-Dspark.master=...'"))
    },
    sparkExecutorURI := {
        sys.props.get("spark.executor.uri").getOrElse(error("spark.executor.uri not defined. Use '-Dspark.executor.uri=...'"))
    },
    unmanagedJars in Compile ++= {
        val sparkHomeFile = file(sparkHome.value)
        val sparkJar = (sparkHomeFile / "lib") * "spark-assembly-*.jar"
        sparkJar.classpath
    }
  )
