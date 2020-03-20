package mops.portfolios.controller;


import lombok.AllArgsConstructor;
import mops.portfolios.AccountService;
import mops.portfolios.domain.portfolio.templates.Template;
import mops.portfolios.domain.portfolio.templates.TemplateService;
import mops.portfolios.tools.AsciiDocConverter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrgaController {

  @Autowired
  private transient TemplateService templateService;

  @Autowired
  private transient final AccountService accountService;

  private transient AsciiDocConverter asciiConverter;

  /**
   * List mapping for GET requests.
   *
   * @param model The Spring Model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/list")
  @RolesAllowed({"ROLE_orga"})
  public String listTemplates(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    List<Template> templateList = templateService.getAll();

    model.addAttribute("templateList", templateList);

    return "orga/list";
  }

  /**
   * Create mapping for GET requests.
   *
   * @param model The spring model to add the attributes to
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/create")
  @RolesAllowed({"ROLE_orga"})
  public String createTemplate(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    return "orga/create";
  }

  /**
   * Edit mapping for GET requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The ID of the template
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/edit")
  @RolesAllowed({"ROLE_orga"})
  public String editTemplate(Model model, @RequestParam Long templateId, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    Template template = templateService.getById(templateId);

    model.addAttribute("template", template);

    return "orga/edit";
  }

  /**
   * View mapping for GET requests.
   *
   * @param model      The spring model to add the attributes to
   * @param templateId The ID of the template
   * @return The page to load
   */
  @SuppressWarnings("PMD")
  @GetMapping("/view")
  @RolesAllowed({"ROLE_orga"})
  public String viewTemplate(Model model, @RequestParam Long templateId, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    Template template = templateService.getById(templateId);

    model.addAttribute("template", template);

    return "orga/view";
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
  public String uploadAscii(Model model, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    model.addAttribute("templateList", templateService.getAll());

    return "orga/asciidoc/upload";
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
  @RolesAllowed({"ROLE_orga"})
  public String viewUploadedAscii(Model model, @RequestParam("file") MultipartFile file, KeycloakAuthenticationToken token) {
    accountService.authorize(model, token);

    byte[] fileBytes;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return "orga/asciidoc/upload";
    }

    String text = new String(fileBytes, StandardCharsets.UTF_8);
    String html = asciiConverter.convertToHtml(text);
    model.addAttribute("html", html);

    return "orga/asciidoc/view";
  }
}
