package com.asyaminor.lib

/**
 * Created by eunlu on 09/12/2015.
 */
trait Common {
  def fold(f: (Int, Int) => Int)(base: Int, start:Int, end: Int): Int = {
    def iter(a: Int, result: Int): Int = {
      if (a > end) result
      else iter(a + 1, f(a, result))
    }

    iter(start, base)
  }
}
