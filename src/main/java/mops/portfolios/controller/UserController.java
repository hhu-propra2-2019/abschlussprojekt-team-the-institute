package mops.portfolios.controller;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.controller.services.FileService;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.UserService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/portfolio/user")
@RolesAllowed({"ROLE_studentin"})
@AllArgsConstructor
public class UserController {

  private transient AccountService accountService;
  private transient UserService userService;
  private transient PortfolioService portfolioService;
  private transient EntryService entryService;
  private final transient FileService fileService = new FileService();

  /**
   * Redirect to main page.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @GetMapping("")
  public String redirect(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    return "redirect:/portfolio/user/list";
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
  public String viewPortfolio(Model model, KeycloakAuthenticationToken token,
                              @RequestParam Long portfolioId,
                              @RequestParam(required = false) Long entryId) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);

    model.addAttribute("portfolio", portfolio);

    if (entryId == null && !portfolio.getEntries().isEmpty()) {
      entryId = portfolio.getEntries().stream().findFirst().get().getId();
    }

    if (entryId != null) {
      Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
      model.addAttribute("Entry", entry);
    }

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

    List<Portfolio> templateList = portfolioService.findAllTemplates();

    model.addAttribute("templateList", templateList);

    return "user/create";
  }

  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
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
    return "redirect:/portfolio/user/view";
  }


  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
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

    return "redirect:/portfolio/user/view";
  }

  /**
   * Post Mapping to update EntryField Content.
   * @param model - Spring MVC model
   * @param token - KeycloakAuthenticationToken
   * @param redirect - injects RedirectAttributes
   * @param portfolioId - Id of current portfolio
   * @param entryId - Id of current entry
   * @param entryFieldId - Id of updated EntryField
   * @param newContent - new content of entryfield
   * @return - redirects to /view
   */
  @SuppressWarnings("PMD")
  @PostMapping("/update")
  public String updateFields(Model model,
                             KeycloakAuthenticationToken token,
                             RedirectAttributes redirect,
                             @RequestParam Long portfolioId,
                             @RequestParam Long entryId,
                             @RequestParam Long entryFieldId,
                             @RequestParam("content") String newContent) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = entryService.findFieldById(entry, entryFieldId);

    String[] content = field.getContent().split(";");
    content[1] = newContent;
    field.setContent(content[0] + ";" + content[1] + ";" + content[2]);
    entryService.update(entry);

    // Sind portfiolioId != portfolio.getId() && entryId != entry.getId() ?
    redirect.addAttribute("portfolioId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());
    return "redirect:/portfolio/user/view";
  }

  /**
   * Post Mapping to update EntryField Content.
   * @param model - Spring MVC model
   * @param token - KeycloakAuthenticationToken
   * @param redirect - injects RedirectAttributes
   * @param portfolioId - Id of current portfolio
   * @param entryId - Id of current entry
   * @param entryFieldId - Id of updated EntryField
   * @param newContent - new content of entryfield
   * @return - redirects to /view
   */
  @SuppressWarnings("PMD")
  @PostMapping("/updateRadio")
  public String updateRadio(Model model,
                            KeycloakAuthenticationToken token,
                             RedirectAttributes redirect,
                            @RequestParam Long portfolioId,
                             @RequestParam Long entryId,
                            @RequestParam Long entryFieldId,
                             @RequestParam("button") List<String> newContent) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = entryService.findFieldById(entry, entryFieldId);

    // System.out.println(field.getContent());

    String[] content = field.getContent().split(";");
    String[] values = content[2].split(",");
    int i = 0;
    for(String nC : newContent) {
      if (nC.equals("checked")) {
        values[i] = nC;
      }
      i++;
    }
    content[2] = "";
    for (String v : values){
      content[2] += v + ",";
    }

    field.setContent(content[0] + ";" + content[1] + ";" + content[2].stripTrailing());

    entryService.update(entry);

    // Sind portfiolioId != portfolio.getId() && entryId != entry.getId() ?
    redirect.addAttribute("portfolioId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());
    return "redirect:/portfolio/user/view";
  }

  /**
   * Post Mapping to update EntryField Content.
   * @param model - Spring MVC model
   * @param token - KeycloakAuthenticationToken
   * @param redirect - injects RedirectAttributes
   * @param portfolioId - Id of current portfolio
   * @param entryId - Id of current entry
   * @param entryFieldId - Id of updated EntryField
   * @param newContent - new content of entryfield
   * @return - redirects to /view
   */
  @SuppressWarnings("PMD")
  @PostMapping("/updateSlider")
  public String updateSlider(Model model,
                            KeycloakAuthenticationToken token,
                            RedirectAttributes redirect,
                            @RequestParam Long portfolioId,
                            @RequestParam Long entryId,
                            @RequestParam Long entryFieldId,
                            @RequestParam("value") String newContent) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = entryService.findFieldById(entry, entryFieldId);

    String[] content = field.getContent().split(";");
    String[] values = content[1].split(",");
    values[2] = newContent;
    field.setContent(content[0] + ";" + values[0] + "," + values[1] + "," + values[2] + ";" + content[2]);

    System.out.println(field.getContent());
    entryService.update(entry);

    // Sind portfiolioId != portfolio.getId() && entryId != entry.getId() ?
    redirect.addAttribute("portfolioId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());
    return "redirect:/portfolio/user/view";
  }

  /**
   * Upload Template mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param portfolioId The id of the portfolio
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @PostMapping("/uploadFile")
  public String uploadFile(Model model,
                               KeycloakAuthenticationToken token,
                               RedirectAttributes redirect,
                               @RequestParam Long portfolioId,
                               @RequestParam Long entryId,
                               @RequestParam Long entryFieldId,
                               @RequestParam("file") MultipartFile file) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = entryService.findFieldById(entry, entryFieldId);

    if (fileService.nothingUploaded(file)) {
      redirect.addAttribute("portfolioId", portfolio.getId());
      redirect.addAttribute("entryId", entry.getId());
      return "redirect:/portfolio/user/view";
    }

    byte[] fileBytes = fileService.readFile(file);



    redirect.addAttribute("portfolioId", portfolio.getId());
    redirect.addAttribute("entryId", entry.getId());
    return "redirect:/portfolio/user/view";
  }
}