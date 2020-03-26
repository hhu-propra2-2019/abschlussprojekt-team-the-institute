package mops.portfolios.domain.portfolio;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.NonNull;
import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class PortfolioService {

  @NonNull final
  transient PortfolioRepository repository;

  public PortfolioService(PortfolioRepository repository) {
    this.repository = repository;
  }

  /**
   * Returns all portfolios and templates in the repository.
   *
   * @return - all Portfolios and Templates
   */
  public List<Portfolio> findAll() {
    return repository.findAll();
  }

  public List<Portfolio> findAllByUserId(String userId) {
    return repository.findAllByUserId(userId);
  }

  public List<Portfolio> findAllByGroupId(Long groupId) {
    return repository.findAllByGroupId(groupId);
  }

  public List<Portfolio> findAllByGroupList(List<Group> groups) {
    List<Long> ids = groups.stream().map(Group::getId).collect(Collectors.toList());
    return repository.findAllByGroupIdIn(ids);
  }

  public Portfolio findPortfolioById(Long id) {
    return repository.findById(id).get();
  }

  /**
   * Finds all actual non-template portfolios.
   *
   * @return - list of portfolios
   */
  public List<Portfolio> findAllPortfolios() {
    return findAll().stream()
        .filter(portfolio -> !portfolio.isTemplate()).collect(Collectors.toList());
  }

  /**
   * Finds all Portfolios that are templates.
   *
   * @return - list of portfolios
   */
  public List<Portfolio> findAllTemplates() {
    return findAll().stream()
        .filter(Portfolio::isTemplate).collect(Collectors.toList());
  }

  /**
   * Finds the entry of the portfolio with the corresponding id.
   *
   * @param portfolio - the portfolio
   * @param id        - the id of the entry
   * @return - the entry.
   */
  @SuppressWarnings("PMD")
  public Entry findEntryInPortfolioById(Portfolio portfolio, Long id) {
    for (Entry entry : portfolio.getEntries()) {
      if (entry.getId().equals(id)) {
        return entry;
      }
    }
    return null;
  }

  /**
   * Finds the last entry of a portfolio.
   *
   * @param portfolio - the portfolio
   * @return - the entry.
   */
  @SuppressWarnings("PMD")
  public Entry findLastEntryInPortfolio(Portfolio portfolio) {
    Iterator<Entry> iterator = portfolio.getEntries().iterator();
    Entry lastEntry = null;
    while (iterator.hasNext()) {
      lastEntry = iterator.next();
    }
    return lastEntry;
  }

  public Portfolio update(Portfolio portfolio) {
    return repository.save(portfolio);
  }

  public void deletePortfolioById(Long id) {
    repository.deleteById(id);
  }

  /**
   * Creates and adds an EntryField to an Entry.
   * @param portfolio the portfolio the entry belongs to
   * @param entryId the id of the entry the field is to be added
   * @param title title of the field to be added
   * @param content content of the field to be added
   */
  public void createAndAddField(Portfolio portfolio, Long entryId, String title, String content) {
    Entry entry;
    if (findEntryInPortfolioById(portfolio,entryId) != null) {
      entry = findEntryInPortfolioById(portfolio, entryId);
    } else {
      entry = new Entry();
    }

    EntryField field = new EntryField();
    field.setTitle(title);
    field.setContent(content);

    entry.getFields().add(field);
  }

  public Entry getNewEntry(@RequestParam Long entryId, @RequestParam("question") String question, Portfolio portfolio) {
    Entry entry;
    if (findEntryInPortfolioById(portfolio,entryId) != null) {
      entry = findEntryInPortfolioById(portfolio, entryId);
    } else {
      entry = new Entry();
    }

    Set<EntryField> fields = entry.getFields();
    EntryField field = new EntryField();
    field.setTitle(question);
    field.setContent(AnswerType.TEXT + ";Some hint");
    fields.add(field);

    entry.setFields(fields);
    update(portfolio);
    return entry;
  }

  public Portfolio getPortfolioWithNewEntry(@RequestParam Long portfolioId, @RequestParam("title") String title) {
    Objects.requireNonNull(portfolioId);
    DemoDataGenerator dataGenerator = new DemoDataGenerator();

    Portfolio portfolio;
    if (findPortfolioById(portfolioId) != null) {
      portfolio = findPortfolioById(portfolioId);
    } else {
      portfolio = new Portfolio();
    }

    Entry entry = new Entry(title);
    entry.setFields(dataGenerator.generateTemplateEntryFieldSet());
    Set<Entry> newEntries = portfolio.getEntries();
    newEntries.add(entry);
    portfolio.setEntries(newEntries);
    update(portfolio);
    return portfolio;
  }

  public void templateEntryCreation(RedirectAttributes redirect, @RequestParam Long templateId, @RequestParam("title") String title) {
    Portfolio portfolio = findPortfolioById(templateId);
    Entry entry = new Entry(title);
    portfolio.getEntries().add(entry);

    portfolio = update(portfolio);
    entry = findLastEntryInPortfolio(portfolio);

    redirect.addAttribute("templateId", templateId);
    redirect.addAttribute("entryId", entry.getId());
  }

  public void templateFieldCreation(RedirectAttributes redirect, @RequestParam Long templateId, @RequestParam Long entryId, @RequestParam("question") String question, @RequestParam("fieldType") String fieldType, @RequestParam(value = "hint", required = false) String hint) {
    Portfolio portfolio = findPortfolioById(templateId);

    createAndAddField(portfolio, entryId,
            question, AnswerType.valueOf(fieldType) + ";" + hint);
    update(portfolio);

    redirect.addAttribute("templateId", templateId);
    redirect.addAttribute("entryId", entryId);
  }

  public void templateCreation(KeycloakAuthenticationToken token, RedirectAttributes redirect, @RequestParam("title") String title) {
    User user = new User();
    user.setName(token.getName()); // FIXME: Nutzen wir auch an jeder Stelle diese Methode? \
    // Geht es ohne user id auch klar?

    Portfolio portfolio = new Portfolio(title, user);
    portfolio.setTemplate(true);
    portfolio = update(portfolio);

    redirect.addAttribute("templateId", portfolio.getId());
  }

  public void getTemplatesToView(Model model, @RequestParam Long templateId, @RequestParam(required = false) Long entryId) {
    Portfolio template = findPortfolioById(templateId);
    model.addAttribute("template", template);

    if (entryId == null && !template.getEntries().isEmpty()) {
      entryId = template.getEntries().stream().findFirst().get().getId();
    }

    if (entryId != null) {
      Entry entry = findEntryInPortfolioById(template, entryId);
      model.addAttribute("templateEntry", entry);
    }
  }

}
