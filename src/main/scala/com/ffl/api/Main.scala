package com.ffl.api

import akka.actor.{Props, ActorRef, ActorSystem}
import akka.io.IO
import com.ffl.api.Actors.UserAuthentication.UserAuthenticationApiActor
import com.ffl.api.DAL.DAO.MongoConnectionContext
import com.typesafe.config.ConfigFactory
import spray.can.Http

/**
 * Package: com.ffl.api
 * Created by zandrewitte on 2015/09/30.
 */
object Main extends App with MongoConnectionContext  {
  val conf = ConfigFactory.load()
  val sprayConf = conf.getConfig("spray")

  implicit val system = ActorSystem.create("ffl_api")

  val user_api = system.actorOf(Props(new UserAuthenticationApiActor(_db)), "user_authentication_integration_api")
  bindListener(user_api, "user_auth_port")


  private def bindListener(api: ActorRef, apiPortConf: String): Unit ={

    IO(Http) ! Http.Bind(listener = api,
      interface = "0.0.0.0",
      port = sprayConf.getInt(apiPortConf))
  }

}
