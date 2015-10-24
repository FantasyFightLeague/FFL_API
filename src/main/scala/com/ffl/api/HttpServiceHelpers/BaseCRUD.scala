package com.ffl.api.HttpServiceHelpers

import akka.actor.{ActorContext, ActorRef}
/**
 * Package: com.fullfacing.ticketing.api
 * Created by zandrewitte on 2015/10/21.
 */
trait BaseCRUD[R] extends BaseRest {

  override def actorRefFactory(implicit ac: ActorContext) = ac

//  def List(implicit actor: ActorRef, manifestA: Manifest[GetAllRequest], manifestB: Manifest[List[R]]) = createPost[GetAllRequest, List[R]]("list")
//  def GetById(implicit actor: ActorRef, manifestA: Manifest[GetByIdRequest], manifestB: Manifest[List[R]]) = createPost[GetByIdRequest, List[R]]("getById")
//  def Save(implicit actor: ActorRef, manifestA: Manifest[SaveRequest[R]], manifestB: Manifest[Option[R]]) = createPost[SaveRequest[R], Option[R]]("save")
//  def Update(implicit actor: ActorRef, manifestA: Manifest[UpdateRequest[R]], manifestB: Manifest[Option[R]]) = createPost[UpdateRequest[R], Option[R]]("update")

}
