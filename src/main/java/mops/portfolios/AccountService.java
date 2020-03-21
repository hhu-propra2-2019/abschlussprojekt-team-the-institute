package mops.portfolios;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.security.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@AllArgsConstructor
public class AccountService {
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
            ((KeycloakPrincipal) token.getPrincipal()).getName());
  }

  public void authorize(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    @SuppressWarnings("PMD")
    KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
    model.addAttribute("account", account);
  }

  public String getUserName(KeycloakAuthenticationToken token) {
    return ((KeycloakPrincipal) token.getPrincipal()).getName();
  }

  public String getOrgaRole(KeycloakAuthenticationToken token) {
    return token.getAccount().getRoles().toString();
  }

  public boolean isOrga(KeycloakAuthenticationToken token) {
    return token.getAccount().getRoles().contains("orga");
  }

}