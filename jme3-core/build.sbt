// Project details
name := "jme3-core"
version := "3.1"

// Use Java 7
javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-g:lines")

// Disable Scala compilation
crossPaths := false
autoScalaLibrary := false

libraryDependencies += "junit" % "junit" % "4.12" % "test"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.0.28-beta" % "test"
libraryDependencies += "org.easytesting" % "fest-assert-core" % "2.0M10" % "test"
