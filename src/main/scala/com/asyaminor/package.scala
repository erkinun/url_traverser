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
}
