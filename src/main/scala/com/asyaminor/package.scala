package com

import java.io.{FileWriter, PrintWriter}

import scala.util.Try
import java.net.URL

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

  def accAvg(avg: Long, index: Int, responseTime: Long): Long = {
    ((avg * (index - 1)) + responseTime) / index
  }

  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
    try { f(param) } finally { param.close() }

  def appendToFile(fileName:String, textData:String) =
    using (new FileWriter(fileName, true)){
      fileWriter => using (new PrintWriter(fileWriter)) {
        printWriter => printWriter.println(textData)
      }
    }

  def getHost(url: String): Try[String] = {
    Try {
      new URL(url).getHost
    }
  }
}
