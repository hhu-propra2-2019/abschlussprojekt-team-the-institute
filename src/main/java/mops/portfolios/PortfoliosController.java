package mops.portfolios;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.keycloak.Account;
import mops.portfolios.tools.AsciiDocConverter;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
public class PortfoliosController {
  private transient HardMock hardMock;
  private transient AsciiDocConverter asciiConverter;
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
        ((KeycloakPrincipal) token.getPrincipal()).getKeycloakSecurityContext().getIdToken()
                    .getPicture(),
        token.getAccount().getRoles(),
        ((KeycloakPrincipal) token.getPrincipal()).getKeycloakSecurityContext().getIdToken()
                 .getSubject());
  }

    private static final Logger log = LoggerFactory.getLogger(PortfoliosApplication.class);

    private void authorize(Model model, KeycloakAuthenticationToken token) {
        Account account = createAccountFromPrincipal(token);
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        model.addAttribute("account", account);
        log.info("--------------- 6666 --------------",principal.getKeycloakSecurityContext().getIdToken().getEmail());
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

    List<Portfolio> p = hardMock.getMockPortfolios();
    List<Portfolio> q = hardMock.getMockGroupPortfolios();

    model.addAttribute("last", q.get(1));
    model.addAttribute("gruppen", q);
    model.addAttribute("vorlesungen", p);
    return "startseite";
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
    List<Portfolio> p = hardMock.getMockPortfolios();
    List<Portfolio> q = hardMock.getMockGroupPortfolios();

    model.addAttribute("gruppen", q);
    model.addAttribute("vorlesungen", p);
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
    List<Portfolio> q = hardMock.getMockGroupPortfolios();

    model.addAttribute("gruppen", q);
    
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
    List<Portfolio> p = hardMock.getMockPortfolios();
    
    model.addAttribute("vorlesungen", p);

    return "privat";
  }

  /**
   * portfolio mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @param title The name of the portfolio
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/portfolio")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})

  public String clickPortfolio(Model model, @RequestParam String title,
                               KeycloakAuthenticationToken token) {

    authorize(model, token);
  
    Portfolio portfolio = hardMock.getPortfolioByTitle(title);

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entries", hardMock.getMockEntry());
  
    if (getOrgaRole(token).contains("orga")) {
      return "portfolio";
    } else if (userSecurity.hasUserId(getUserId(token))) {
      return "portfolio";
    } else {
      return "redirect://localhost:8080";
    }
  }

  /**
   * entry mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @param title The name of the entry
   * @param entryTitle The title of the entry
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/entry")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickEntry(Model model, @RequestParam String title,
                           @RequestParam String entryTitle, KeycloakAuthenticationToken token) {

    authorize(model, token);
    Portfolio portfolio = hardMock.getPortfolioByTitle(title);
    Entry entry = hardMock.getEntryByTitle(entryTitle);

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entry", entry);
    return "entry";
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
  public String editTemplate(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

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
  @RolesAllowed({"ROLE_orga"})
  public String uploadTemplate(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

    model.addAttribute("portfolioList", hardMock.getMockPortfolios());
    model.addAttribute("entryList", hardMock.getMockEntry());

    return "upload_template";
  }

  /**
   * View mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @param file The uploaded (AsciiDoc) template file
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @PostMapping("/view")
  @RolesAllowed({"ROLE_orga"})
  public String viewUploadedTemplate(Model model, @RequestParam("file") MultipartFile file,
                                     KeycloakAuthenticationToken token) {
    authorize(model, token);

    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return "upload_template";
    }

    String text = new String(fileBytes, StandardCharsets.UTF_8);
    String html = asciiConverter.convertToHtml(text);
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

}
