package com.ffl.api.DAO

import java.io.FileInputStream
import java.util.Properties

import com.ffl.api.Entity.EUser
import com.mongodb.casbah.{MongoClient, MongoDB}
import com.mongodb.{DBObject, ServerAddress}
import org.slf4j.LoggerFactory
/**
 * Package: com.fullfacing.dsl.DAO
 * Created by zandrewitte on 2015/10/08.
 */

trait MongoConnectionContext extends ConnectionContext[MongoDB] {
  val llog = LoggerFactory.getLogger("MongoConnectionContext")
  val _db = connect()
  private def connect(): MongoDB = {
    val configFile = System.getProperties.getProperty("mongo.file", "conf/mongo.properties")
    llog.info(s"${Console.GREEN}System Property mongo.file${Console.WHITE}: ${Console.BLUE}${System.getProperties.getProperty("mongo.file","not found")}${Console.RESET}")
    llog.info(s"${Console.GREEN}Mongo config file used:${Console.WHITE}: ${Console.BLUE}$configFile${Console.RESET}")
    val properties = loadProperties(configFile)
    val host = properties.getProperty("host")
    val port = properties.getProperty("port")
    val dbName = lookupDbName(properties)
    val server = new ServerAddress(host, port.toInt)
    val client = MongoClient(server)
    client(dbName)
  }

  def lookupDbName(properties: Properties): String = {
    properties.getProperty("dbName")
  }

  private def loadProperties(fileName: String): Properties = {
    val properties = new Properties()
    properties.load(new FileInputStream(fileName))
    properties
  }
}

trait MongoContext extends DBContext[MongoDB, DBObject, DBObject, DBObject] {
  val _db: MongoDB

  // USER AUTHENTICATION
  override def users = FFLMongoDAO[EUser](_db("users"))

}