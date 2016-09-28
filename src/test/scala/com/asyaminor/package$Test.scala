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

}
