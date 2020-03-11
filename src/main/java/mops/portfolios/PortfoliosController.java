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
import mops.portfolios.Portfolio.Portfolio;
import mops.portfolios.keycloak.Account;
import org.asciidoctor.Asciidoctor;
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
  @GetMapping("/")
  public String requestList(Model model) {
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

  @GetMapping("/index")
  public String requestIndex(Model model) {
    return "index";
  }

  @GetMapping("/gruppen")
  public String requestGruppen(Model model) {
    return "gruppen";
  }

  @GetMapping("/privat")
  public String requestPrivate(Model model) {
    return "privat";
  }

  @GetMapping("/portfolio")
  public String clickPortfolio(Model model, @RequestParam String title) {

    Portfolio portfolio = getPortfolioByTitle(title);
    if (portfolio == null) {
      return null;
    }

    model.addAttribute("portfolio", portfolio);

    return "portfolio";
  }

  @GetMapping("/entry")
  public String clickEntry(Model model, @RequestParam String title, @RequestParam int id) {

    Portfolio portfolio = getPortfolioByTitle(title);
    if (portfolio == null) {
      return null;
    }

    Entry entry = getEntryById(portfolio, id);
    if (entry == null) {
      return null;
    }

    model.addAttribute("portfolio", portfolio);
    model.addAttribute("entry", entry);

    return "entry";
  }

  @GetMapping("/upload")
  public String upload(Model model) {
    return "upload";
  }

  @PostMapping("/view")
  public String uploadFile(Model model, @RequestParam("file") MultipartFile uploadedFile) {

    System.out.println("RECEIVED FILE " + uploadedFile.getOriginalFilename());

    try {
      String text = new String(uploadedFile.getBytes(), StandardCharsets.UTF_8);
      String html = convertAsciiDocTextToHTML(text);
      model.addAttribute("html", html);
      System.out.println("GOT TEXT" + html);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("GOT ERROR");
    }

    return "view";
  }


  @GetMapping("/logout")
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

  private transient List<Portfolio> portfolioList = Arrays.asList(
      new Portfolio(),
      new Portfolio(),
      new Portfolio());

  /**
   * returns portfolio with corresponding title
   */
  @SuppressWarnings("PMD")
  private Portfolio getPortfolioByTitle(String title) {
    for (Portfolio portfolio : portfolioList) {
      if (portfolio.getTitle().equals(title)) {
        return portfolio;
      }
    }
    return null;
  }

  /**
   * returns entry with corresponding id
   */
  @SuppressWarnings("PMD")
  private Entry getEntryById(Portfolio portfolio, int id) {
    /*
    for (Entry entry : portfolio) {
      if (entry.getId() == id) {
        return entry;
      }
    }
     */
    return new Entry("0", null);
  }

  /**
   * convert ascii to html
   */
  @SuppressWarnings("PMD")
  private String convertAsciiDocTextToHTML(String asciiDocText) {
    Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    String html = asciidoctor.convert(asciiDocText, new HashMap<>());
    asciidoctor.close();
    return html;
  }

}