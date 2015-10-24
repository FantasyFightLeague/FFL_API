package com.ffl.api.HttpServiceHelpers

import akka.actor.{ActorContext, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import com.ffl.api.DAL.Framework.JSONAtomizers._
import com.ffl.api.Model.ResponseContainer
import com.ffl.api.Common.ResponseConverter._
import org.slf4j.LoggerFactory
import spray.routing.HttpService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}
/**
 * Package: com.fullfacing.ticketing.api
 * Created by zandrewitte on 2015/10/21.
 */
trait BaseRest extends HttpService {

  def actorRefFactory(implicit ac: ActorContext) = ac

  implicit val timeout = Timeout(60 seconds)

  val logger = LoggerFactory.getLogger(getClass)

  def createPost[A <: AnyRef, B](url: String)(implicit actor: ActorRef, manifestA: Manifest[A], manifestB: Manifest[B]) = {
    path(url){
      post {
        extract {
          _.request.entity.asString
        } { entity =>
          logger.info(s"${Console.WHITE}Request(${Console.GREEN}$entity)${Console.WHITE}:::  ${Console.MAGENTA}${entity.deatomized[A]}${Console.WHITE} ::: on URL : ${Console.BLUE}$url ${Console.RESET}")

          onComplete((actor ? entity.deatomized[A]).mapTo[ResponseContainer[B]]){
            case Success(res) => res
            case Failure(ex) => ex
          }
        }
      }
    }
  }

}
