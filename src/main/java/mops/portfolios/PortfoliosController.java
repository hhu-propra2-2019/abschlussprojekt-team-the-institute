package mops.portfolios;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.HashMap;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.Domain.Portfolio.Portfolio;
import mops.portfolios.Domain.UserGroup.User;
import mops.portfolios.keycloak.Account;
import org.asciidoctor.Asciidoctor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private transient UserSecurity userSecurity;

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

  private void authorize(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);
  }

  private String getUserId(KeycloakAuthenticationToken token) {
    return token.getAccount().getKeycloakSecurityContext().getIdToken().getId();
  }

  private String getOrgaRole(KeycloakAuthenticationToken token) {
    return token.getAccount().getRoles().toString();
  }

  /**
   * Root mapping for GET requests.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestList(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);
    String userId = getUserId();
    model.addAttribute("last", getLastPortfolio(userId));
    model.addAttribute("gruppen", getGruppenPortfolios(userId));
    model.addAttribute("vorlesungen", getVorlesungPortfolios(userId));
    return "startseite";
  }

  private String getUserId() {
    return "";
  }

  private String[] getLastPortfolio(String userId) {
    return new String[]{"0", "Software Entwicklung im Team", "" + userId, null};
  }

  private String[][] getGruppenPortfolios(String userId) {
    return new String[][]{{"1", "Praktikum", null, "" + userId}};
  }

  private String[][] getVorlesungPortfolios(String userId) {
    return new String[][]{{"0", "Software Entwicklung im Team", "" + userId, null},{"2", "Machine Learning", "" + userId, null}};
  }

  /**
   * Index mapping for GET requests.
   *
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/index")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestIndex(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

    return "index";

  }

  /**
   * Group mapping for GET requests.
   *
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/gruppen")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestGruppen(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

    return "gruppen";

  }

  /**
   * Individual portfolios mapping for GET requests.
   *
   */

  @SuppressWarnings("PMD")
  @GetMapping("/privat")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestPrivate(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

    return "privat";

  }

  /**
   * Portfolio mapping for GET requests.
   * @param model The spring model to add the attributes to
   * @param title The name of the portfolio
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/portfolio")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickPortfolio(Model model, @RequestParam String title, KeycloakAuthenticationToken token) {
    authorize(model, token);

    Set<String> roles = new HashSet<>(Arrays.asList("student"));
    User user1 = new User("User Name", "mail@example.com", null, roles, "UUID-1234-1234");
    Portfolio portfolio = new Portfolio("Praktikum", user1);

    model.addAttribute("portfolio", portfolio);

    if (getOrgaRole(token).contains("orga")) {
      return "portfolio";
    } else if (userSecurity.hasUserId(getUserId(token))) {
      return "portfolio";
    } else {
      return "redirect://localhost:8080";
    }
  }

  /**
   * Entry mapping for GET requests.
   * @param model The spring model to add the attributes to
   * @param title The name of the entry
   * @param id The id of the entry
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/entry")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickEntry(Model model, @RequestParam String title, @RequestParam int id, KeycloakAuthenticationToken token) {
    authorize(model, token);

    return "entry";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/upload")
  @RolesAllowed({"ROLE_orga"})
  public String upload(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);
    return "upload";
  }

  /**
   * View mapping for GET requests.
   *
   */

  @SuppressWarnings("PMD")
  @PostMapping("/view")
  @RolesAllowed({"ROLE_orga"})
  public String uploadFile(Model model, @RequestParam("file") MultipartFile uploadedFile, KeycloakAuthenticationToken token) {
    authorize(model, token);
    System.out.println("RECEIVED FILE " + uploadedFile.getOriginalFilename());

    try {
      String text = new String(uploadedFile.getBytes(), StandardCharsets.UTF_8);
      String html = convertAsciiDocTextToHtml(text);
      model.addAttribute("html", html);
      System.out.println("GOT TEXT" + html);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("GOT ERROR");
    }

    return "view";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/logout")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

  /**
   * convert ascii to html.
   */

  @SuppressWarnings("PMD")
  private String convertAsciiDocTextToHtml(String asciiDocText) {
    Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    String html = asciidoctor.convert(asciiDocText, new HashMap<>());
    asciidoctor.close();
    return html;
  }

}
