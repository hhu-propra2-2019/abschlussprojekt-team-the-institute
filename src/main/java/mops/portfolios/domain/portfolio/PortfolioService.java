package mops.portfolios.domain.portfolio;

import java.util.*;
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

@Service
public class PortfolioService {

  private @NonNull final transient PortfolioRepository repository;

  private static final String requestTitle = "title";
  private static final String requestTemplateId = "templateId";

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

  /**
   * Gets new entry and updates portfolio.
   *
   * @return new Entry
   */
  public Entry getNewEntry(@RequestParam Long entryId,
                           @RequestParam("question") String question, Portfolio portfolio) {
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

  /**
   * Gets portfolio with new entry.
   *
   * @return new portfolio
   */

  public Portfolio getPortfolioWithNewEntry(@RequestParam Long portfolioId,
                                            @RequestParam(requestTitle) String title) {
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

  /**
   * Gets new template.
   * @return new Template
   */
  public Portfolio getTemplate(KeycloakAuthenticationToken token,
                               @RequestParam(requestTitle) String title) {
    User user = new User();
    user.setName(token.getName()); // FIXME: Nutzen wir auch an jeder Stelle diese Methode? \
    // Geht es ohne user id auch klar?

    Portfolio portfolio = new Portfolio(title, user);
    portfolio.setTemplate(true);
    portfolio = update(portfolio);
    return portfolio;
  }

  /**
   * Gets portfolio/template view.
   *
   */
  public void getPortfoliosTemplatesToView(Model model,
                                           @RequestParam Long portfolioId,
                                           @RequestParam(required = false) Long entryId,
                                           String entryName) {

    Portfolio portfolio = findPortfolioById(portfolioId);

    if (entryId == null && !portfolio.getEntries().isEmpty()) {
      entryId = portfolio.getEntries().stream().findFirst().get().getId();
    }

    if (entryId != null) {
      Entry entry = findEntryInPortfolioById(portfolio, entryId);
      model.addAttribute(entryName, entry);
    }
  }

  /**
   * Gets entry.
   * @return entry
   */

  public Entry getEntry(@RequestParam Long portfolioId, @RequestParam Long entryId) {

    Portfolio portfolio = findPortfolioById(portfolioId);
    Entry entry = findEntryInPortfolioById(portfolio, entryId);
    return entry;
  }

  /**
   * Gets portfolio and checks if it is a template.
   * @return portfolio
   */
  public Portfolio getPortfolio(KeycloakAuthenticationToken token,
                                @RequestParam(value = requestTemplateId, required = false)
                                        String templateId,
                                @RequestParam(value = requestTitle, required = false)
                                        String title,
                                @RequestParam("isTemplate") String isTemplate) {

    User user = new User();
    user.setName(token.getName()); //FIXME

    Portfolio portfolio;
    if (isTemplate.equals("true")) {

      Portfolio template = findPortfolioById(Long.valueOf(templateId));
      portfolio = new Portfolio(template.getTitle(), user);

      //FIXME: clone template into portfolio? how are we going to save answers..
    } else {
      portfolio = new Portfolio(title, user);
    }

    portfolio = update(portfolio);
    return portfolio;
  }

  /**
   * Creates an entry.
   * @return created entry
   */

  public Entry portfolioEntryCreation(@RequestParam Long portfolioId,
                                      @RequestParam(requestTitle) String title) {

    Portfolio portfolio = findPortfolioById(portfolioId);
    Entry entry = new Entry(title);
    portfolio.getEntries().add(entry);

    portfolio = update(portfolio);
    entry = findLastEntryInPortfolio(portfolio);
    return entry;
  }

}