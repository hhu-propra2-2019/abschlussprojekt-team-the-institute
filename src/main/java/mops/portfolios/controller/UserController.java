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
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;
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
@RolesAllowed({"ROLE_studentin"})
@AllArgsConstructor
public class UserController {

  private transient AccountService accountService;
  private transient UserService userService;
  private transient PortfolioService portfolioService;
  private transient EntryService entryService;

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

    List<Portfolio> templateList = portfolioService.findAllTemplates();

    model.addAttribute("groupPortfolios", groupPortfolios);
    model.addAttribute("userPortfolios", userPortfolios);
    model.addAttribute("allPortfolios", allPortfolios);

    model.addAttribute("templateList", templateList);

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
   * Create Template Entry mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @PostMapping("/entry")
  public String createEntry(Model model, KeycloakAuthenticationToken token,
                            RedirectAttributes redirectAttributes,
                            @RequestParam Long portfolioId,
                            @RequestParam("title") String title) {
    accountService.authorize(model, token);
    DemoDataGenerator dataGenerator = new DemoDataGenerator();

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = new Entry(title);
    entry.setFields(dataGenerator.generateTemplateEntryFieldSet());
    Set<Entry> newEntries = portfolio.getEntries();
    newEntries.add(entry);
    portfolio.setEntries(newEntries);
    portfolioService.update(portfolio);

    // Ist portofolioId und portfolio.getId() unterschiedlich?
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
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);

    Set<EntryField> fields = entry.getFields();
    EntryField field = new EntryField();
    field.setTitle(question);
    field.setContent(AnswerType.TEXT + ";Some hint");
    fields.add(field);

    entry.setFields(fields);
    portfolioService.update(portfolio);

    // Sind portfiolioId != portfolio.getId() && entryId != entry.getId() ?
    redirect.addAttribute("templateId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());

    return "redirect:/user/view";
  }

  /**
   * Post Mapping to update EntryField Content.
   *
   * @param model        - Spring MVC model
   * @param token        - KeycloakAuthenticationToken
   * @param redirect     - injects RedirectAttributes
   * @param portfolioId  - Id of current portfolio
   * @param entryId      - Id of current entry
   * @param entryFieldId - Id of updated EntryField
   * @param newContent   - new content of entryfield
   * @return - redirects to /view
   */
  @PostMapping("/update")
  public String updateFields(Model model, KeycloakAuthenticationToken token,
                             RedirectAttributes redirect, @RequestParam Long portfolioId,
                             @RequestParam Long entryId, @RequestParam Long entryFieldId,
                             @RequestParam("content") String newContent) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = entryService.findFieldById(entry, entryFieldId);

    field.setContent(newContent);
    entryService.update(entry);

    // Sind portfiolioId != portfolio.getId() && entryId != entry.getId() ?
    redirect.addAttribute("portfolioId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());
    return "redirect:/user/view";
  }

  /**
   * Create Portfolio mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId Id of the template if chosen
   * @param title      New portfolio title if no template is chosen
   * @param isTemplate True if it's a template portfolio, false if it's a personal portfolio
   * @return The page to load
   */
  @PostMapping("/createPortfolio")
  public String createPortfolio(Model model,
                                KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                @RequestParam(value = "templateId", required = false) String templateId,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam("isTemplate") String isTemplate) {
    accountService.authorize(model, token);

    User user = new User();
    user.setName(token.getName()); //FIXME

    Portfolio portfolio;
    if (isTemplate.equals("true")) {

      Portfolio template = portfolioService.findPortfolioById(Long.valueOf(templateId));
      portfolio = new Portfolio(template.getTitle(), user);

      //FIXME: clone template into portfolio? how are we going to save answers..
    } else {
      portfolio = new Portfolio(title, user);
    }

    portfolio = portfolioService.update(portfolio);

    redirect.addAttribute("portfolioId", portfolio.getId());

    return "redirect:/user/view";
  }
}
