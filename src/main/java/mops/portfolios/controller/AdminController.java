package mops.portfolios.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.Template;
import mops.portfolios.domain.portfolio.templates.TemplateService;
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
@RequestMapping("/admin")
@RolesAllowed({"ROLE_orga"})
@AllArgsConstructor
public class AdminController {

  private transient AccountService accountService;

  private transient PortfolioService portfolioService;
  private transient TemplateService templateService;

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

    return "redirect:/admin/list";
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

    List<Template> templateList = templateService.getAll();

    model.addAttribute("templateList", templateList);

    return "admin/list";
  }

  /**
   * Create mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @PostMapping("/create")
  public String createTemplate(Model model, KeycloakAuthenticationToken token,
                               RedirectAttributes redirect, @RequestParam("title") String title) {
    accountService.authorize(model, token);

    User user = new User();
    user.setName(token.getName());

    Portfolio portfolio = new Portfolio(title, user);
    portfolio.setTemplate(true);
    portfolio = portfolioService.save(portfolio);

    redirect.addAttribute("templateId", portfolio.getId());

    return "redirect:/admin/view";
  }

  /**
   * View mapping for GET requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The ID of the template
   * @return The page to load
   */
  @GetMapping("/view")
  public String viewTemplate(Model model, @RequestParam Long templateId,
                             KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    //Portfolio portfolio = portfolioService.findPortfolioById(templateId);

    Template template = templateService.getById(templateId);

    model.addAttribute("template", template);

    return "admin/view";
  }

  /**
   * Upload mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @GetMapping("/upload")
  public String uploadAscii(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    model.addAttribute("templateList", templateService.getAll());

    return "admin/asciidoc/upload";
  }

  /**
   * View mapping for POST requests.
   *
   * @param model The spring model to add the attributes to
   * @param file  The uploaded (AsciiDoc) template file
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @PostMapping("/viewAscii")
  public String viewUploadedAscii(Model model, @RequestParam("file") MultipartFile file,
                                  KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return "admin/asciidoc/upload";
    }

    String text = new String(fileBytes, StandardCharsets.UTF_8);
    String html = asciiConverter.convertToHtml(text);
    model.addAttribute("html", html);

    return "admin/asciidoc/view";
  }
}
