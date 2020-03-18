package mops.portfolios;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.Port;
import lombok.AllArgsConstructor;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioRepository;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.usergroup.UserGroupService;
import mops.portfolios.keycloak.Account;
import mops.portfolios.tools.AsciiDocConverter;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  private transient AsciiDocConverter asciiConverter;
  private transient UserSecurity userSecurity;

  @Autowired
  private transient EntryService entryService;
  @Autowired
  private transient PortfolioService portfolioService;
  @Autowired
  private transient UserGroupService userGroupService;

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

  private void authorize(Model model, KeycloakAuthenticationToken token) {
    Account account = createAccountFromPrincipal(token);
    model.addAttribute("account", account);
  }

  private String getUserName(KeycloakAuthenticationToken token) {
    return ((KeycloakPrincipal) token.getPrincipal()).getName();
  }

  private String getOrgaRole(KeycloakAuthenticationToken token) {
    return token.getAccount().getRoles().toString();
  }

  @SuppressWarnings("PMD")
  private List<Portfolio> getPortfolios(KeycloakAuthenticationToken token, List<Portfolio> p) {
    List<Portfolio> portfolios = new ArrayList<>();

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
    
    List<Portfolio> portfoliosList = portfolioService.findFirstFew();

    List<Portfolio> groupPortfolios = /*getPortfolios(token, */portfoliosList.subList(0, 4);
    List<Portfolio> userPortfolios = getPortfolios(token,
        portfoliosList.subList(4, portfoliosList.size() - 1));

    model.addAttribute("last", groupPortfolios.get(0));
    model.addAttribute("gruppen", groupPortfolios);
    model.addAttribute("vorlesungen", userPortfolios);

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

    List<Portfolio> groupPortfolios = getPortfolios(token,
        portfolioService.getGroupPortfolios(userGroupService, "userId"));
    List<Portfolio> userPortfolios = getPortfolios(token,
        portfolioService.findAllByUserId("userId"));

    model.addAttribute("gruppen", groupPortfolios);
    model.addAttribute("vorlesungen", userPortfolios);

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

    List<Portfolio> groupPortfolios = getPortfolios(token,
        portfolioService.getGroupPortfolios(userGroupService, "userId"));

    model.addAttribute("gruppen", groupPortfolios);

    return "gruppen";
  }

  /**
   * Individual portfolios mapping for GET requests.
   */
  @SuppressWarnings("PMD")
  @GetMapping("/privat")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String requestPrivate(Model model, KeycloakAuthenticationToken token) {
    authorize(model, token);

    List<Portfolio> userPortfolios = getPortfolios(token,
        portfolioService.findAllByUserId("userId"));

    model.addAttribute("vorlesungen", userPortfolios);

    return "privat";
  }

  /**
   * portfolio mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @param id    The ID of the portfolio
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/portfolio")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickPortfolio(Model model, @RequestParam Long id,
                               KeycloakAuthenticationToken token) {

    authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(id);

    model.addAttribute("portfolio", portfolio);

    if (getOrgaRole(token).contains("orga")) {
      return "portfolio";
    } else if (userSecurity.hasUserName(getUserName(token))) {
      return "portfolio";
    } else {
      return "redirect://localhost:8081"; // TODO: redirect to error page
    }
  }

  /**
   * entry mapping for GET requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The portfolio id
   * @param entryId     The entry id
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/entry")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String clickEntry(Model model, @RequestParam Long portfolioId,
                           @RequestParam Long entryId, KeycloakAuthenticationToken token) {

    authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryById(portfolio, entryId);

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

    model.addAttribute("portfolioList", portfolioService.findAll());

    return "upload_template";
  }

  /**
   * View mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @param file  The uploaded (AsciiDoc) template file
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
