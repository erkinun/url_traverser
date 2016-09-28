package com.asyaminor

import org.scalatest.FunSuite

/**
  * Created by eunlu on 28/09/2016.
  */
class package$Test extends FunSuite {

  test("testValidateUrl adding http to a normal hostname") {
    assert("http://sporapp.com" === validateUrl("sporapp.com"))
  }

  test("test validateUrl shall not add anything more to the hostname if it has http://") {
    assert("http://sporapp.com" === validateUrl("http://sporapp.com"))
  }

  test("accumulating average function should return 1 after adding 1 to 0") {
    assert(accAvg(0, 1, 1) === 1)
  }


  test("acc average function should return 4 after adding 3 to 5") {
    assert(accAvg(5, 2, 3) === 4)
  }

}
