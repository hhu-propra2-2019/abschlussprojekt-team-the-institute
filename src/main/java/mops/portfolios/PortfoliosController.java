package mops.portfolios;

import java.util.Arrays;
import java.util.List;
import mops.portfolios.objects.Portfolio;
import mops.portfolios.objects.PortfolioEntry;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.Domain.UserGroup.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private transient Group group;


  /**
   * Takes the auth-token from Keycloak and generates an AccounDTO for the views.
   *
   * @param token the auth-token from Keycloak
   * @return new Account to be used in the templates
   */
  private Account createAccountFromPrincipal(KeycloakAuthenticationToken token) {
    KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
    return new Account(
            principal.getName(),
            principal.getKeycloakSecurityContext().getIdToken().getEmail(),
            null,
            token.getAccount().getRoles());
  }

  /**
   * Root mapping for GET requests.
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
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

  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }
  
  
    private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio("Propra1"),
      new Portfolio("Propra2"),
      new Portfolio("Algorithmen_und_Datenstrukturen"));

  /**
   * returns portfolio with corresponding title
   */
  @SuppressWarnings("PMD")
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
  @SuppressWarnings("PMD")
  private PortfolioEntry getEntryById(Portfolio portfolio, int id) {
    for (PortfolioEntry entry : portfolio.getEntries()) {
      if (entry.getId() == id) {
        return entry;
      }
    }
    return null;
  }

}
