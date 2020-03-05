package mops.portfolios;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InitController {

  @GetMapping("/")
  public String requestList(Model model) {
    model.addAttribute("portfolioList", portfolioList);
    return "index";
  }

  @GetMapping("/portfolio")
  public String requestElement(Model model, @RequestParam String course) {
    model.addAttribute("portfolio", getPortfolioByCourse(course));
    return "element";
  }

  private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio("Propra1"),
      new Portfolio("Propra2"),
      new Portfolio("Algorithmen_und_Datenstrukturen"));

  @SuppressWarnings("PMD")
  private Portfolio getPortfolioByCourse(String course) {
    for (Portfolio portfolio : portfolioList) {
      if (portfolio.getCourse().equals(course)) {
        return portfolio;
      }
    }
    return null;
  }
}