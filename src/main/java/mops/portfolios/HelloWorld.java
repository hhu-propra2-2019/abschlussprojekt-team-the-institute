package mops.portfolios;

import org.springframework.stereotype.Service;

@Service
class HelloWorld {

  String greeting(String s) {
    return "Hello, " + s + "!";
  }
}
