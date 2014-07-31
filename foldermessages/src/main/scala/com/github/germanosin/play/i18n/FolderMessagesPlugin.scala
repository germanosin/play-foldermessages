package com.github.germanosin.play.i18n

import play.api.Application
import play.api.i18n.Lang
import play.api.i18n.{MessagesPlugin, MessagesApi, Messages}
import play.api.i18n.Messages.UrlMessageSource
import play.utils.Resources
import java.net.URL
import com.github.germanosin.play.utils.DirIO


/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 28.07.14
 * Time: 21:32
 */



class FolderMessagesPlugin(app: Application) extends MessagesPlugin {

  import scala.collection.JavaConverters._


  private lazy val messagesPrefix = app.configuration.getString("messages.path")
  private lazy val pluginEnabled = app.configuration.getString("foldermessagesplugin")

  private def joinPaths(first: Option[String], second: String) = first match {
    case Some(first) => new java.io.File(first, second).getPath
    case None => second
  }

  protected def loadMessages(file: String): Map[String, String] = {
    app.classloader.getResources(joinPaths(messagesPrefix, file)).asScala.toList.filterNot(Resources.isDirectory).reverse.map { messageFile =>
      loadMessages(messageFile)
    }.foldLeft(Map.empty[String, String]) { _ ++ _ }
  }

  protected def loadMessages(messageFile: URL): Map[String, String] =
      Messages.messages(UrlMessageSource(messageFile), messageFile.toString).fold(e => throw e, identity)

  def renameKeys(elements: Map[String,String], prefix:String):Map[String,String] = {
      elements.map {
       case (key,value) => (prefix+"."+key,value)
      }.toMap
  }


  def loadFilesForLang(lang:String) = {
    app.classloader.getResources(joinPaths(messagesPrefix, lang)).asScala.toList.filter(Resources.isDirectory).map {
      messageFolder => {
        DirIO.readFolderFiles(messageFolder)(app).map {
          iter => renameKeys(loadMessages(iter._1), iter._2)
        }.foldLeft(Map.empty[String, String]) { _ ++ _ }
      }
    }.foldLeft(Map.empty[String, String]) { _ ++ _ }
  }

  protected def messages = {
    Lang.availables(app).map(_.code).map {
      lang => (lang,loadFilesForLang(lang))
    }.toMap
      .+("default" -> loadMessages("messages"))
      .+("default.play" -> loadMessages("messages.default"))
  }


  /**
   * Is this plugin enabled.
   *
   * {{{
   * defaultmessagesplugin=disabled
   * }}}
   */
  override def enabled = pluginEnabled.forall(_ != "disabled")

  /**
   * The underlying internationalisation API.
   */
  lazy val api = MessagesApi(messages)

  /**
   * Loads all configuration and message files defined in the classpath.
   */
  override def onStart() = api

}
