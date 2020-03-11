package mops.portfolios;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.Entry.Entry;
import mops.portfolios.Portfolio.*;
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
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/")
  public String requestList(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);
    int user_id = getUserId();
    model.addAttribute("last", getLastPortfolio(user_id));
    model.addAttribute("gruppen", getGruppenPortfolios(user_id));
    model.addAttribute("vorlesungen", getVorlesungPortfolios(user_id));
    return "startseite";
  }

  private int getUserId() {
    return 0;
  }

  private String[] getLastPortfolio(int user_id) {
    return new String[]{"0", "Software Entwicklung im Team", ""+user_id, null};
  }

  private String[][] getGruppenPortfolios(int user_id) {
    return new String[][]{{"1", "Praktiukm", null, ""+user_id}};
  }

  private String[][] getVorlesungPortfolios(int user_id) {
    return new String[][]{{"0", "Software Entwicklung im Team", ""+user_id, null},{"2", "Machine Learning", ""+user_id, null}};
  }

  @SuppressWarnings("PMD")
  @GetMapping("/index")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestIndex(Model model) {
    return "index";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/gruppen")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestGruppen(Model model) {
    return "gruppen";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/privat")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestPrivate(Model model) {
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
  public String clickPortfolio(Model model, @RequestParam String title) {
    Portfolio portfolio = new Portfolio("Praktikum", new User("Test123"));

    model.addAttribute("portfolio", portfolio);

    return "portfolio";
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
  public String clickEntry(Model model, @RequestParam String title, @RequestParam int id) {

    return "entry";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/upload")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String upload(Model model) {
    return "upload";
  }

  /**
   * View mapping for GET requests.
   *
   */

  @SuppressWarnings("PMD")
  @PostMapping("/view")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String uploadFile(Model model, @RequestParam("file") MultipartFile uploadedFile) {

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