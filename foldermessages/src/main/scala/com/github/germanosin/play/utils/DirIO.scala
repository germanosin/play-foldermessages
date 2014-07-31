package com.github.germanosin.play.utils

import play.api.Application
import java.net.{JarURLConnection, URL}
import java.io.File
import scala.reflect.io.{Path, Directory}

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.07.14
 * Time: 22:28
 */
object DirIO {

  import scala.collection.JavaConverters._

  def readFolderFiles(messagesFolder: URL)(implicit app:Application):Iterator[(URL,String)] = {
    messagesFolder.getProtocol match {
      case "file" =>  Directory(Path(new File(messagesFolder.toURI))).files.map( file => (file.toURL, file.name) ).toIterator
      case "jar" => readJarFolder(messagesFolder)
      case _ => throw new IllegalArgumentException(s"Cannot read directory for a URL with protocol='${messagesFolder.getProtocol}'")
    }
  }

  private def readJarFolder(jarFolder: URL)(implicit app:Application) = {
    val con = jarFolder.openConnection().asInstanceOf[JarURLConnection]
    val jarFile = con.getJarFile

    val rootEntryPath = Some(con.getJarEntry()) match {
      case Some(entry) => entry.getName
      case _ => ""
    }

    jarFile.entries().asScala.filter( e => e.getName.startsWith(rootEntryPath)).map {
      entry =>  (app.classloader.getResource(entry.getName), entry.getName.replace(rootEntryPath, ""))
    }.toIterator
  }
}
