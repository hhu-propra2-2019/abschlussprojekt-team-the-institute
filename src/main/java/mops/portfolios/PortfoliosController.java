package mops.portfolios;

import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.keycloak.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private transient HelloWorld greeter;
  private transient Group group;

  private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio("Propra 1"),
      new Portfolio("Propra 2"),
      new Portfolio("Algorithmen und Datenstrukturen"));


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
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String testGreet(KeycloakAuthenticationToken token, Model model) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);
    
    model.addAttribute("text", greeter.greeting(account.getName()));

    model.addAttribute("portfolioList", portfolioList);

    return "test";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

}