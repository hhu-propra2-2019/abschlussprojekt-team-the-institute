package mops.portfolios.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.file.FileService;
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
  private final transient FileService fileService;
  private final String portfolioIdAttribute = "portfolioId";
  private final String entryIdAttribute = "entryId";

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
  public String viewPortfolio(Model model, KeycloakAuthenticationToken token,
                              @RequestParam Long portfolioId,
                              @RequestParam(required = false) Long entryId) {
    accountService.authorize(model, token);
    model.addAttribute("portfolio", portfolioService.findPortfolioById(portfolioId));
    model.addAttribute("fileService", fileService);

    portfolioService.getPortfoliosTemplatesToView(model, portfolioId, entryId, "portfolioEntry");

    return "user/view";
  }

  /**
   * Create portfolio entry mapping for POST requests.
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
    Portfolio portfolio = portfolioService.getPortfolioWithNewEntry(portfolioId, title);
    redirectAttributes.addAttribute(portfolioIdAttribute, portfolio.getId());

    return "redirect:/portfolio/user/view";
  }

  /**
   * Create portfolio field mapping for POST requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The id of the portfolio
   * @param entryId     The id of the entry
   * @param question    The question (title) of the new field
   * @param hint        The hint (data) of the new field
   * @return The page to load
   */
  @PostMapping("/createPortfolioField")
  public String createPortfolioField(Model model,
                                     KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                     @RequestParam Long portfolioId,
                                     @RequestParam Long entryId,
                                     @RequestParam("question") String question,
                                     @RequestParam("fieldType") String fieldType,
                                     @RequestParam(value = "hint", required = false) String hint) {
    accountService.authorize(model, token);
    Portfolio portfolio = portfolioService.findPortfolioById(portfolioId);

    AnswerType answerType = AnswerType.valueOf(fieldType);
    portfolioService.createAndAddField(portfolio, entryId, question, answerType, hint);
    portfolioService.update(portfolio);

    redirect.addAttribute("entryId", entryId);
    redirect.addAttribute("portfolioId", portfolioId);
    return "redirect:/portfolio/user/view";
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

    redirect.addAttribute(portfolioIdAttribute, portfolioId);
    entryService.updateEntryFields(entryFieldId, newContent, entry);
    redirect.addAttribute(entryIdAttribute, entryId);

    return "redirect:/portfolio/user/view";
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
                                @RequestParam(value = "templateId", required = false)
                                    String templateId,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam("isTemplate") String isTemplate) {
    accountService.authorize(model, token);

    Portfolio portfolio = portfolioService.getNewPortfolio(token, templateId, title, isTemplate);

    redirect.addAttribute(portfolioIdAttribute, portfolio.getId());

    return "redirect:/portfolio/user/view";
  }


  /**
   * Create Portfolio Entry mapping for POST requests.
   *
   * @param model       The spring model to add the attributes to
   * @param portfolioId The id of the portfolio
   * @param title       The title of the new entry
   * @return The page to load
   */
  @PostMapping("/createPortfolioEntry")
  public String createPortfolioEntry(Model model,
                                     KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                     @RequestParam Long portfolioId,
                                     @RequestParam("title") String title) {
    accountService.authorize(model, token);

    Entry entry = portfolioService.portfolioEntryCreation(portfolioId, title);

    redirect.addAttribute(portfolioIdAttribute, portfolioId);
    redirect.addAttribute(entryIdAttribute, entry.getId());

    return "redirect:/portfolio/user/view";
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

    entryService.updateEntryFieldCheck(newContent, entry, field);

    redirect.addAttribute(portfolioIdAttribute, portfolio.getId());
    redirect.addAttribute(entryIdAttribute, entry.getId());
    return "redirect:/portfolio/user/view";
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

    entryService.updateEntryFieldSlider(newContent, field);

    entryService.update(entry);

    redirect.addAttribute(portfolioIdAttribute, portfolio.getId());
    redirect.addAttribute(entryIdAttribute, entry.getId());
    return "redirect:/portfolio/user/view";
  }

  /**
   * Upload Template mapping for POST requests.
   *
   * @param model       The spring model to add the attributes to
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
      redirect.addAttribute(portfolioIdAttribute, portfolio.getId());
      redirect.addAttribute(entryIdAttribute, entry.getId());
      return "redirect:/portfolio/user/view";
    }

    fileService.updateField(file, field);
    entryService.update(entry);

    redirect.addAttribute(portfolioIdAttribute, portfolio.getId());
    redirect.addAttribute(entryIdAttribute, entry.getId());
    return "redirect:/portfolio/user/view";
  }

  /**
   * Delete Portfolio mapping for POST requests.
   */
  @PostMapping("/deletePortfolio")
  public String deletePortfolio(Model model,
                                KeycloakAuthenticationToken token,
                                @RequestParam Long portfolioId) {
    accountService.authorize(model, token);
    portfolioService.deletePortfolioById(portfolioId);

    return "redirect:/portfolio/user/list";
  }
}

