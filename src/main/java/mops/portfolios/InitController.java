package mops.portfolios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InitController {

  private final HelloWorld greeter;

  public InitController(HelloWorld greeter) {
    this.greeter = greeter;
  }

  @GetMapping("/")
  public String testGreet(Model model) {
    model.addAttribute("text", greeter.greeting("Tester"));
    return "test";
  }
}