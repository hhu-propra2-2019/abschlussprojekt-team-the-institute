package mops.portfolios.controller;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.portfolio.templates.Template;
import mops.portfolios.domain.portfolio.templates.TemplateService;
import mops.portfolios.domain.user.UserService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RolesAllowed( {"ROLE_studentin"})
@AllArgsConstructor
public class UserController {

  private transient AccountService accountService;
  private transient UserService userService;
  private transient PortfolioService portfolioService;

  /**
   * Redirect to main page.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @GetMapping("")
  public String redirect(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    return "redirect:/user/list";
  }

  /**
   * List mapping for GET requests.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @GetMapping("/list")
  public String listPortfolios(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    String userName = accountService.getUserName(token);

    List<Group> groups = userService.getGroupsByUserName(userName);
    // TODO Implement optional sublisting with method overload in portfolioService
    List<Portfolio> groupPortfolios = portfolioService.findAllByGroupList(groups);
    List<Portfolio> userPortfolios = portfolioService.findAllByUserId(userName);
    List<Portfolio> allPortfolios = Stream.of(userPortfolios, groupPortfolios)
        .flatMap(Collection::stream).collect(Collectors.toList());

    model.addAttribute("groupPortfolios", groupPortfolios);
    model.addAttribute("userPortfolios", userPortfolios);
    model.addAttribute("allPortfolios", allPortfolios);

    return "user/list";
  }

  /**
   * View mapping for GET requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The ID of the portfolio
   * @return The page to load
   */
  @GetMapping("/view")
  public String viewPortfolio(Model model, @RequestParam Long portfolioId,
                              KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);

    model.addAttribute("portfolio", portfolio);

    return "user/view";
  }

  /**
   * Create mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @GetMapping("/create")
  public String createPortfolio(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    List<Portfolio> templateList = portfolioService.getAllTemplates();

    model.addAttribute("templateList", templateList);

    return "user/create";
  }
  
  /**
   * Submit mapping for GET requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The portfolio id
   * @return The page to load
   */
  @GetMapping("/submit")
  public String submitPortfolio(Model model, @RequestParam Long portfolioId,
                                KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Template template = templateService.convertPortfolioToTemplate(portfolio);

    model.addAttribute("template", template);

    return "user/submit";
  }

  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @PostMapping("/entry")
  public String createEntry(Model model, KeycloakAuthenticationToken token,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam Long portfolioId, @RequestParam("title") String title) {
    accountService.authorize(model, token);
    DemoDataGenerator dataGenerator = new DemoDataGenerator();

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = new Entry(title);
    entry.setFields(dataGenerator.generateTemplateEntryFieldSet(entry));
    Set<Entry> newEntries = portfolio.getEntries();
    newEntries.add(entry);
    portfolio.setEntries(newEntries);
    portfolioService.update(portfolio);

    redirectAttributes.addAttribute("portfolioId", portfolio.getId());

    System.out.println("Updated");
    return "redirect:/user/view";
  }


  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @PostMapping("/createField")
  public String createField(Model model,
                                    KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                    @RequestParam Long portfolioId, @RequestParam Long entryId,
                                    @RequestParam("question") String question) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryById(portfolio, entryId);

    Set<EntryField> fields = entry.getFields();
    EntryField field = new EntryField();
    field.setTitle(question);
    field.setContent(AnswerType.TEXT + ";Some hint");
    fields.add(field);

    entry.setFields(fields);
    portfolioService.update(portfolio);

    redirect.addAttribute("templateId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());

    return "redirect:/user/view";
  }
}
