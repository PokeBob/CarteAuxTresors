name := """PlayJavaTreasuresApp"""
organization := "carbon-it.a-meyer"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.11"

libraryDependencies += guice
