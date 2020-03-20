package mops.portfolios.controller;

import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private transient final AccountService accountService;

  /**
   * Redirect to right path depending on user role
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String redirect(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    if (accountService.isOrga(token)) {
      return "redirect:admin/";
    }

    return "redirect:user/";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/logout")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

}
