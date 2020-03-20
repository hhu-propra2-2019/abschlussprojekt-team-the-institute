package mops.portfolios.controller;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.domain.portfolio.templates.Template;
import mops.portfolios.domain.portfolio.templates.TemplateService;
import mops.portfolios.security.UserSecurity;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.usergroup.UserGroupService;
import mops.portfolios.tools.AsciiDocConverter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
  private transient TemplateService templateService;
  @Autowired
  private transient UserGroupService userGroupService;
  @Autowired
  private transient final AccountService accountService;

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
    accountService.authorize(model, token);
    
    List<Portfolio> portfoliosList = portfolioService.findFirstFew();

    List<Portfolio> groupPortfolios = accountService
            .getGroupPortfolios(token, portfoliosList.subList(0, 4));
    List<Portfolio> userPortfolios = accountService.getPortfolios(token,
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
    accountService.authorize(model, token);

    List<Portfolio> groupPortfolios = accountService.getGroupPortfolios(token,
            portfolioService.getGroupPortfolios(userGroupService, "userId"));
    List<Portfolio> userPortfolios = accountService.getPortfolios(token,
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
    accountService.authorize(model, token);

    List<Portfolio> groupPortfolios = accountService.getGroupPortfolios(token,
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
    accountService.authorize(model, token);

    List<Portfolio> userPortfolios = accountService.getPortfolios(token,
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

    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(id);

    model.addAttribute("portfolio", portfolio);

    if (accountService.getOrgaRole(token).contains("orga")) {
      return "portfolio";
    } else if (userSecurity.hasUserName(accountService.getUserName(token))) {
      return "portfolio";
    } else {
      return "../error";
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

    accountService.authorize(model, token);

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
    accountService.authorize(model, token);

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
    accountService.authorize(model, token);

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
    accountService.authorize(model, token);

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

  /**
   * Submit mapping for GET requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The portfolio id
   * @return The page to load
   */
  @GetMapping("/submit")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String submit(Model model, /*@RequestParam Long portfolioId,*/ KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    //Template template = templateService.getById(portfolioId);
    Template template = templateService.getByTitle("Propra2");

    model.addAttribute("template", template);

    return "submit";
  }

  @SuppressWarnings("PMD")
  @GetMapping("/logout")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String logout(HttpServletRequest request) throws Exception {
    request.logout();
    return "redirect:/";
  }

  public UserGroupService getUserGroupService() {
    return userGroupService;
  }
}
