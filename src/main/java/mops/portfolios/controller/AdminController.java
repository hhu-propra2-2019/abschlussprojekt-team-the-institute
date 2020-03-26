package mops.portfolios.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.controller.services.FileService;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.user.User;
import mops.portfolios.tools.AsciiDocConverter;
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
@RequestMapping("/portfolio/admin")
@RolesAllowed({"ROLE_orga"})
@AllArgsConstructor
public class AdminController {
  private final transient FileService fileService = new FileService();
  private final transient EntryService entryService = new EntryService();

  private transient AccountService accountService;

  private transient PortfolioService portfolioService;

  private transient AsciiDocConverter asciiConverter;

  /**
   * Redirect to main page.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @GetMapping("")
  public String redirect(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    return "redirect:/portfolio/admin/list";
  }

  /**
   * List mapping for GET requests.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @GetMapping("/list")
  public String listTemplates(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    List<Portfolio> templateList = portfolioService.findAllTemplates();

    model.addAttribute("templateList", templateList);

    return "admin/list";
  }

  /**
   * View mapping for GET requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The ID of the template
   * @param entryId    The ID of the entry
   * @return The page to load
   */
  @GetMapping("/view")
  public String viewTemplate(Model model, KeycloakAuthenticationToken token,
                             @RequestParam Long templateId,
                             @RequestParam(required = false) Long entryId) {
    accountService.authorize(model, token);
    model.addAttribute("template", portfolioService.findPortfolioById(templateId));


    portfolioService.getTemplatesToView(model, templateId, entryId);

    return "admin/view";
  }



  /**
   * Create Template mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @param title The title of the new template
   * @return The page to load
   */
  @PostMapping("/createTemplate")
  public String createTemplate(Model model,
                               KeycloakAuthenticationToken token, RedirectAttributes redirect,
                               @RequestParam("title") String title) {
    accountService.authorize(model, token);

    accountService.authorize(model, token);
    Portfolio portfolio = portfolioService.getTemplate(token, title);

    redirect.addAttribute("templateId", portfolio.getId());

    return "redirect:/portfolio/admin/view";
  }


  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The id of the template
   * @param title      The title of the new entry
   * @return The page to load
   */
  @PostMapping("/createTemplateEntry")
  public String createTemplateEntry(Model model,
                                    KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                    @RequestParam Long templateId,
                                    @RequestParam("title") String title) {
    accountService.authorize(model, token);

    Entry entry = portfolioService.portfolioEntryCreation(templateId,title);

    redirect.addAttribute("templateId", templateId);
    redirect.addAttribute("entryId", entry.getId());

    return "redirect:/portfolio/admin/view";
  }



  /**
   * Create Template Entry mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The id of the template
   * @param entryId    The id of the entry
   * @param question   The question (title) of the new field
   * @param hint       The hint (data) of the new field
   * @return The page to load
   */
  @PostMapping("/createTemplateField")
  public String createTemplateField(Model model,
                                    KeycloakAuthenticationToken token, RedirectAttributes redirect,
                                    @RequestParam Long templateId,
                                    @RequestParam Long entryId,
                                    @RequestParam("question") String question,
                                    @RequestParam("fieldType") String fieldType,
                                    @RequestParam(value = "hint", required = false) String hint) {
    accountService.authorize(model, token);

    portfolioService.templateFieldCreation(templateId, entryId, question, fieldType, hint);

    redirect.addAttribute("entryId", entryId);
    redirect.addAttribute("templateId", templateId);
    return "redirect:/portfolio/admin/view";
  }

  /**
   * Delete Template mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The id of the template
   * @return The page to load
   */
  @PostMapping("/deleteTemplate")
  public String deleteTemplate(Model model,
                               KeycloakAuthenticationToken token,
                               @RequestParam Long templateId) {
    accountService.authorize(model, token);
    portfolioService.deletePortfolioById(templateId);

    return "redirect:/portfolio/admin/list";
  }

  /**
   * Upload Template mapping for POST requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The id of the template
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @PostMapping("/uploadTemplate")
  public String uploadTemplate(Model model,
                               KeycloakAuthenticationToken token,
                               @RequestParam Long templateId,
                               @RequestParam("file") MultipartFile file) {
    accountService.authorize(model, token);

    if (fileService.nothingUploaded(file)) {
      return "admin/asciidoc/upload";
    }

    byte[] fileBytes = fileService.readFile(file);

    String text = new String(fileBytes, StandardCharsets.UTF_8);
    String html = asciiConverter.convertToHtml(text);
    model.addAttribute("html", html);

    return "admin/asciidoc/view";
  }

}
