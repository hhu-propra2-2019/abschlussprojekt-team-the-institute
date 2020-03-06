package mops.portfolios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.keycloak.Account;
import org.json.JSONException;
import org.json.JSONObject;
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

  private static final Logger logger = LoggerFactory.getLogger(PortfoliosApplication.class);


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

  /**
   * Use this method to get a List containing a List out of all MatrikelNr in a group.
   * @param matrikelnr The MatrikelNr of the student to get the group members of
   * @return List of the MatrikelNr of each group member
   */
  List<MatrikelNr> getGroupmembers(MatrikelNr matrikelnr) {
    HttpClient httpClient = new HttpClient();
    String responseBody = httpClient.getBody("/gruppen2/groupmembers");

    // Content-Type von gruppen2 ist JSON
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(responseBody);
    } catch (JSONException err) {
      logger.error("Error: " + err.toString());
      //TODO ERROR!!!!!!!!!!!
    }

    // TODO genaues URI mit gruppen2 absprechen

    List<MatrikelNr> matrikelNrList = new ArrayList<>();
    return matrikelNrList;
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

}