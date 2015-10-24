package com.ffl.api.DAL.DAO

/**
 * Package: com.fullfacing.dsl.DAO
 * Created by zandrewitte on 2015/10/08.
 */
trait ConnectionContext[A] { def _db: A }
trait DBContext[A, B, C, Q] {
  val _db: A

  // USER AUTHENTICATION
  def users: DAO[_, B, C, Q]

}
