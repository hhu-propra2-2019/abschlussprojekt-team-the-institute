package mops.portfolios.domain.portfolio;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import mops.portfolios.demodata.DemoDataGenerator;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PortfolioService {

  @Autowired
  transient PortfolioRepository repository;

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
    Entry entry = findEntryInPortfolioById(portfolio, entryId);

    EntryField field = new EntryField();
    field.setTitle(title);
    field.setContent(content);

    entry.getFields().add(field);
  }

  public Entry getNewPortfolio(@RequestParam Long entryId, @RequestParam("question") String question, Portfolio portfolio) {
    Entry entry = findEntryInPortfolioById(portfolio, entryId);

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
    DemoDataGenerator dataGenerator = new DemoDataGenerator();

    Portfolio portfolio = findPortfolioById(portfolioId);
    Entry entry = new Entry(title);
    entry.setFields(dataGenerator.generateTemplateEntryFieldSet());
    Set<Entry> newEntries = portfolio.getEntries();
    newEntries.add(entry);
    portfolio.setEntries(newEntries);
    update(portfolio);
    return portfolio;
  }

}
