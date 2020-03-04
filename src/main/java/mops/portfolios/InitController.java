package mops.portfolios;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class InitController {

  private transient HelloWorld greeter;

  public InitController(HelloWorld greeter) {
    this.greeter = greeter;
  }

  private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio("Propra 1"),
      new Portfolio("Propra 2"),
      new Portfolio("Algorithmen und Datenstrukturen"));

  @GetMapping("/")
  public String testGreet(Model model) {
    model.addAttribute("text", greeter.greeting("Tester"));

    model.addAttribute("portfolioList", portfolioList);

    return "test";
  }
}