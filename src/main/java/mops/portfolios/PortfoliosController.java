package mops.portfolios;

import lombok.AllArgsConstructor;
import mops.portfolios.Domain.Entry.Entry;
import mops.portfolios.Domain.Entry.EntryField;
import mops.portfolios.Domain.Portfolio.Portfolio;
import mops.portfolios.Domain.UserGroup.User;
import mops.portfolios.keycloak.Account;
import mops.portfolios.tools.AsciiDocConverter;
import org.asciidoctor.Asciidoctor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private User getMockUser() {
    return new User("Mocked", "abc@gmail.com", null, Collections.singleton("Student"), "1");
  }

  private List<Portfolio> getMockPortfolios() {
    User user = getMockUser();
    return Arrays.asList(
        new Portfolio("Machine Learning", user),
        new Portfolio("Softwareentwicklung", user)
    );
  }

  private List<Portfolio> getMockGroupPortfolios() {
    User user = getMockUser();
    return Arrays.asList(
        new Portfolio("Elektronik", user),
        new Portfolio("Praktikum", user)
    );
  }

  private List<Entry> getMockEntry() {
    Entry e = new Entry();
    e.setTitle("Test123");
    e.setFields(getMockEntryFields());

    Entry f = new Entry();
    f.setTitle("Test456");
    f.setFields(getMockEntryFields());

    return Arrays.asList(e, f);
  }

  private List<EntryField> getMockEntryFields() {
    EntryField first = new EntryField();
    first.setTitle("First");
    first.setContent("Lore Ipsum");
    EntryField second = new EntryField();
    second.setTitle("Second");
    second.setContent("Veni, vidi, vici");

    return Arrays.asList(first, second);
  }

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
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestList(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);

    List<Portfolio> p = getMockPortfolios();
    List<Portfolio> q = getMockGroupPortfolios();

    model.addAttribute("last", q.get(1));
    model.addAttribute("gruppen", q);
    model.addAttribute("vorlesungen", p);
    return "startseite";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/index")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestIndex(Model model) {
    List<Portfolio> p = getMockPortfolios();
    List<Portfolio> q = getMockGroupPortfolios();
    model.addAttribute("gruppen", q);
    model.addAttribute("vorlesungen", p);
    return "index";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/gruppen")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestGruppen(Model model) {
    List<Portfolio> q = getMockGroupPortfolios();
    model.addAttribute("gruppen", q);
    return "gruppen";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/privat")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestPrivate(Model model) {
    List<Portfolio> p = getMockPortfolios();
    model.addAttribute("vorlesungen", p);
    return "privat";
  }

  /**
   * Portfolio mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @param title The name of the portfolio
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/portfolio")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickPortfolio(Model model, @RequestParam String title) {
    Portfolio portfolio = getPortfolioByTitle(title);

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entries", getMockEntry());
    return "portfolio";
  }

  /**
   * Entry mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @param title The name of the entry
   * @param entry_title The title of the entry
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/entry")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickEntry(Model model, @RequestParam String title, @RequestParam String entry_title) {
    Portfolio portfolio = getPortfolioByTitle(title);
    Entry entry = getEntryByTitle(entry_title);

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entry", entry);
    return "entry";
  }

  private Entry getEntryByTitle(String title) {
    List<Entry> entries = getMockEntry();
    for (Entry e : entries){
      if(e.getTitle().equals(title)){
        return e;
      }
    }
    return null;
  }

  /**
   * Edit mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/edit")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String editTemplate(Model model) {
    return "edit_template";
  }

  /**
   * Upload mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */

  @SuppressWarnings("PMD")
  @GetMapping("/upload")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String uploadTemplate(Model model) {
    return "upload_template";
  }

  /**
   * View mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @param file The uploaded (AsciiDoc) template file
   * @return The page to load
   */

  private transient AsciiDocConverter asciiConverter;

  @SuppressWarnings("PMD")
  @PostMapping("/view")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String viewUploadedTemplate(Model model, @RequestParam("file") MultipartFile file) {

    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return "upload_template";
    }

    String text = new String(fileBytes, StandardCharsets.UTF_8);
    String html = asciiConverter.convertToHTML(text);
    model.addAttribute("html", html);

    return "view_template";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/logout")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

  private Portfolio getPortfolioByTitle(@RequestParam String title) {
    List<Portfolio> p = getMockPortfolios();
    List<Portfolio> q = getMockGroupPortfolios();

    Portfolio portfolio = null;

    for (Portfolio r : p) {
      if (r.getTitle().equals(title)) {
        portfolio = r;
      }
    }

    for (Portfolio r : q) {
      if (r.getTitle().equals(title)) {
        portfolio = r;
      }
    }
    return portfolio;
  }
}
