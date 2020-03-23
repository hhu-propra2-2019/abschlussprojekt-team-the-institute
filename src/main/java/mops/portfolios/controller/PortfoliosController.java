package mops.portfolios.controller;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.domain.portfolio.templates.Template;
import mops.portfolios.domain.portfolio.templates.TemplateService;
import mops.portfolios.security.UserSecurity;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.user.UserService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class PortfoliosController {

  @Autowired
  private transient UserSecurity userSecurity;
  @Autowired
  private transient UserService userService;
  @Autowired
  private transient PortfolioService portfolioService;
  @Autowired
  private transient TemplateService templateService;
  @Autowired
  private transient final AccountService accountService;

  @Autowired
  public PortfoliosController(UserService userService, PortfolioService portfolioService, AccountService accountService) {
    this.userService = userService;
    this.portfolioService = portfolioService;
    this.accountService = accountService;
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

    accountService.authorize(model, token);
    String userName = accountService.getUserName(token);

    List<Group> groups = userService.getGroupsByUserName(userName);
    // TODO Implement optional sublisting with method overload in portfolioService
    List<Portfolio> groupPortfolios = portfolioService.findAllByGroupList(groups);
    List<Portfolio> userPortfolios = portfolioService.findAllByUserId(userName);

    // You should probably do this in view against "gruppen" attribute of the model
    if (groupPortfolios.size() > 0) {
      model.addAttribute("last", groupPortfolios.get(0));
    }

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
    String userName = accountService.getUserName(token);

    List<Group> groups = userService.getGroupsByUserName(userName);

    List<Portfolio> groupPortfolios = portfolioService.findAllByGroupList(groups);
    List<Portfolio> userPortfolios = portfolioService.findAllByUserId(userName);

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
    String userName = accountService.getUserName(token);


    List<Group> groups = userService.getGroupsByUserName(userName);
    List<Portfolio> groupPortfolios = portfolioService.findAllByGroupList(groups);

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
    String userName = accountService.getUserName(token);

    List<Portfolio> userPortfolios = portfolioService.findAllByUserId(userName);

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
    } else if (
        userSecurity.isAllowedToViewOrEditPortfolio(
            accountService.getUserName(token),
            portfolio)) {
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
   * CreatePortfolio mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/createPortfolio")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String createPortfolio(Model model, KeycloakAuthenticationToken token) {

    accountService.authorize(model, token);

    List<Portfolio> portfolioList = portfolioService.findAll();

    model.addAttribute("portfolioList", portfolioList);
    return "create_portfolio";
  }

  /**
   * Submit mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
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

  @SuppressWarnings("PMD")
  @GetMapping("/create_entry")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String createNewEntry(Model model, KeycloakAuthenticationToken token, @RequestParam Long portfolioId) {
    accountService.authorize(model, token);
    model.addAttribute("id", portfolioId);
    return "/create_entry";
  }

  @SuppressWarnings("PMD")
  @PostMapping("/create")
  @RolesAllowed({"ROLE_orga", "ROLE_studentin"})
  public String createNewEntry(Model model, RedirectAttributes redirectAttributes,
                               KeycloakAuthenticationToken token, @RequestParam Long id, String titel) {
    accountService.authorize(model, token);

    redirectAttributes.addAttribute("id", id);
    Portfolio p = portfolioService.findPortfolioById(id);
    List<Entry> e = p.getEntries();
    e.add(new Entry(titel));
    p.setEntries(e);
    // TODO: Update!
    portfolioService.update(p);
    return "redirect:/portfolio";
  }
}
