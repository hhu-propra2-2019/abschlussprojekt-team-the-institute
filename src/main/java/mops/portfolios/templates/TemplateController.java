package mops.portfolios.templates;

import mops.portfolios.keycloak.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;

@Controller
public class TemplateController {

  @Autowired
  private transient TemplateService templateService;

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
        ((KeycloakPrincipal) token.getPrincipal()).getKeycloakSecurityContext().getIdToken()
            .getPicture(),
        token.getAccount().getRoles(),
        ((KeycloakPrincipal) token.getPrincipal()).getKeycloakSecurityContext().getIdToken()
            .getSubject());
  }

  private void authorize(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);
  }

  /**
   * submit mapping for GET requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The portfolio id
   * @param entryId     The entry id
   * @return The page to load
   */
  @GetMapping("/submit")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String submit(Model model, /*@RequestParam Long portfolioId,
                       @RequestParam Long entryId,*/ KeycloakAuthenticationToken token) {
    authorize(model, token);

    Template template = templateService.get("Propra1", "Übung_2");

    model.addAttribute("template", template);

    return "submit";
  }
}
