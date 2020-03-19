package mops.portfolios;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import mops.portfolios.controller.PortfoliosController;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.usergroup.Group;
import mops.portfolios.domain.usergroup.UserGroup;
import mops.portfolios.domain.usergroup.UserGroupService;
import mops.portfolios.security.Account;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@AllArgsConstructor
public class AccountService {
   private final transient UserGroupService userGroupService;
   
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

  @SuppressWarnings("PMD")
  public List<Portfolio> getPortfolios(KeycloakAuthenticationToken token, List<Portfolio> p) {
    List<Portfolio> portfolios = new ArrayList<Portfolio>();

    // TODO: Changing userId since it is dynamic and can therefore not be used

    for (Portfolio portfolio : p) {

      if (portfolio.getUserId() == null) {
        continue;
      }

      if (portfolio.getUserId().equals(getUserName(token))) {
        portfolios.add(portfolio);
      }
    }
    return portfolios;
  }

  @SuppressWarnings("PMD")
  public List<Portfolio> getGroupPortfolios(KeycloakAuthenticationToken token, List<Portfolio> p) {
    List<Portfolio> portfolios = new ArrayList<Portfolio>();
    List<UserGroup> groups = this.userGroupService.findAllByUserId(getUserName(token));
    Portfolio staticPortfolio = new Portfolio("Lorem ipsum", new Group(1L, "Group 1"));

    for (Portfolio portfolio : p) {
      if (portfolio.getGroupId() == null) {
        portfolios.add(staticPortfolio);
      }
      for (UserGroup group : groups) {
        if (portfolio.getGroupId() == group.getGroupId()) {
          portfolios.add(portfolio);
        }
      }
    }
    return portfolios;
  }
}