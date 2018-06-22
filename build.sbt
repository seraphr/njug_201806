
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "njug_201806",
    libraryDependencies += "io.reactivex.rxjava2" % "rxjava" % "2.1.14"
  )
