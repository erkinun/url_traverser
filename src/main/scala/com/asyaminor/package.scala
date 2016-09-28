package com

/**
  * Created by eunlu on 10/09/2016.
  */
package object asyaminor {
  def validateUrl(url: String): String = {
    val fullUrl = if (url.contains("http")) {
      url
    }
    else {
      "http://" + url
    }

    fullUrl
  }

  def time[R](block: => R): (R, Long) = {
    val t0 = System.currentTimeMillis()
    val result = block    // call-by-name
    val t1 = System.currentTimeMillis()
    (result, (t1 - t0))
  }
}
