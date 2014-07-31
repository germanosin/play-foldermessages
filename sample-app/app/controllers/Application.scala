package controllers

import play.api._
import play.api.mvc._
import play.api.i18n.Messages
import play.api.libs.json._
import scala.collection.JavaConverters._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def messagesWithArgs(path:String, args:String) = Action {
    Ok(Messages(path,args))
  }

  def messages(path:String) = Action {
  	 Ok(Messages(path))
  }

  def messagesAll() = Action {
    import play.api.Play.current
    Ok(Json.toJson(Messages.messages))
  }
}