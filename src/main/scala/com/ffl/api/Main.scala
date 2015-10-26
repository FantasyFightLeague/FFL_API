package com.ffl.api

import akka.actor.{Props, ActorRef, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import com.ffl.api.Actors.UserAuthentication.UserAuthenticationApiActor
import com.ffl.api.DAO.MongoConnectionContext
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import spray.can.Http
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
/**
 * Package: com.ffl.api
 * Created by zandrewitte on 2015/09/30.
 */
object Main extends App with MongoConnectionContext  {
  val logger = LoggerFactory.getLogger("com.ffl.api.Main")
  val sprayConf = ConfigFactory.load().getConfig("spray")
  implicit val timeout = Timeout(30 seconds)
  implicit val system = ActorSystem.create("ffl_api")

  val user_api = system.actorOf(Props(new UserAuthenticationApiActor(_db)), "user_authentication_integration_api")
  bindListener(user_api, "user_auth_port")

  private def bindListener(api: ActorRef, apiPortConf: String): Unit ={

    IO(Http) ? Http.Bind(listener = api,
      interface = "0.0.0.0",
      port = sprayConf.getInt(apiPortConf)) onComplete{
      case scala.util.Success(res) => logger.info(s"Bound ${api.path.name} on Port : ${sprayConf.getInt(apiPortConf)}")
      case scala.util.Failure(ex) => logger.info(s"Binding for ${api.path.name} on Port : ${sprayConf.getInt(apiPortConf)} Failed. Reason : ${ex.getMessage}")
    }
  }

}
