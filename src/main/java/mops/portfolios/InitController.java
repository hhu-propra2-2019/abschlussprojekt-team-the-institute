package mops.portfolios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

  @Autowired
  private HelloWorld greeter = new HelloWorld();

  @GetMapping("/")
  public String testGreet(Model model) {
    model.addAttribute("text", greeter.greeting("Tester"));
    return "test";
  }
}
