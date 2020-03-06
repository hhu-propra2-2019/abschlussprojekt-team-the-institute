package mops.portfolios;

import java.util.Arrays;
import java.util.List;

import mops.portfolios.objects.Portfolio;
import mops.portfolios.objects.PortfolioEntry;
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
  public String clickPortfolio(Model model, @RequestParam String title) {

    Portfolio portfolio = getPortfolioByTitle(title);
    if (portfolio == null) {
      return null;
    }

    model.addAttribute("portfolio", portfolio);

    return "portfolio";
  }

  @GetMapping("/entry")
  public String clickEntry(Model model,@RequestParam String title, @RequestParam int id) {

    Portfolio portfolio = getPortfolioByTitle(title);
    if (portfolio == null) {
      return null;
    }

    PortfolioEntry entry = getEntryById(portfolio, id);
    if (entry == null) {
      return null;
    }

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entry", entry);

    return "entry";
  }

  private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio("Propra1"),
      new Portfolio("Propra2"),
      new Portfolio("Algorithmen_und_Datenstrukturen"));

  /**
   * returns portfolio with corresponding title
   */
  private Portfolio getPortfolioByTitle(String title) {
    for (Portfolio portfolio : portfolioList) {
      if (portfolio.getTitle().equals(title)) {
        return portfolio;
      }
    }
    return null;
  }

  /**
   * returns entry with corresponding id
   */
  private PortfolioEntry getEntryById(Portfolio portfolio, int id) {
    for (PortfolioEntry entry : portfolio.getEntries()) {
      if (entry.getId() == id) {
        return entry;
      }
    }
    return null;
  }
}