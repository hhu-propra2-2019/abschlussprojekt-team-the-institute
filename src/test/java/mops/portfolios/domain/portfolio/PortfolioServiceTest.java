package mops.portfolios.domain.portfolio;

import lombok.NonNull;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PortfolioServiceTest {


  private transient PortfolioRepository repository = mock(PortfolioRepository.class);
  @NonNull
  private transient PortfolioService portfolioService = new PortfolioService(repository);
  private transient EntryRepository entryRepository = mock(EntryRepository.class);
  private transient EntryFieldRepository entryFieldRepository = mock(EntryFieldRepository.class);
  private transient KeycloakAuthenticationToken token = mock(KeycloakAuthenticationToken.class);


  @SuppressWarnings("PMD")
  @Test @Disabled
  void createAndAddEntryTest() {

    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    Entry entry = new Entry();
    EntryField field = new EntryField();
    field.setTitle("Question?");
    field.setContent(AnswerType.TEXT + ";" + "Some hint");
    field.setId(1L);
    Set<EntryField> entryFields = new HashSet<>();
    entryFields.add(field);

    when(entryFieldRepository.save(any(EntryField.class))).thenReturn(field);
    entryFieldRepository.save(field);

    entry.setFields(entryFields);
    when(entryRepository.save(any(Entry.class))).thenReturn(entry);
    entryRepository.save(entry);

    portfolioService.createAndAddField(portfolio, 1L, "Question?", AnswerType.TEXT,
        "hint");

    Set<EntryField> newEntryFields = entry.getFields();

    for (EntryField newField: newEntryFields) {
      Assert.assertEquals("EntryField(id=1, title=Question?, content=TEXT;hint; , attachment=null)", newField.toString());
    }

  }

  @Test
  void getPortfolioWithNewEntryTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(repository.findById(1L)).thenReturn(Optional.of(portfolio));

    Portfolio newPortfolio = portfolioService.findPortfolioById(1L);
    Portfolio portfolioWithEntry = portfolioService.getPortfolioWithNewEntry(1L, "Lorem");

    Assert.assertEquals(newPortfolio, portfolioWithEntry);

  }

  @SuppressWarnings("PMD")
  @Test
  void getNewEntryTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    Entry entry = new Entry();
    EntryField field = new EntryField();
    field.setTitle("Question?");
    field.setContent(AnswerType.TEXT + ";" + "Some hint");
    Set<EntryField> entryFields = new HashSet<>();
    entryFields.add(field);

    entry.setFields(entryFields);
    when(entryRepository.save(any(Entry.class))).thenReturn(entry);
    entryRepository.save(entry);

    Entry newEntry = portfolioService.getNewEntry(1L, "Question?", portfolio);

    Set<EntryField> fields = newEntry.getFields();

    for (EntryField newField: fields) {
      Assert.assertEquals("EntryField(id=null, title=Question?, content=TEXT;Some hint, attachment=null)", newField.toString());
    }

  }

  @Test
  void getTemplateTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);
    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(token.getName()).thenReturn("studentin");

    Portfolio updatedPortfolio = portfolioService.getTemplate(token, "Lorem");

    Assert.assertEquals(portfolio, updatedPortfolio);

  }

  @Test
  void getPortfolioTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);
    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(token.getName()).thenReturn("studentin");

    when(repository.findById(1L)).thenReturn(Optional.of(portfolio));

    Portfolio updatedPortfolio = portfolioService.getNewPortfolio(token, "1" , "Lorem", "true");

    Assert.assertEquals(portfolio, updatedPortfolio);


  }

  @Test
  void portfolioEntryCreationTest() {
    User user = new User();
    user.setName("studentin");
    Entry entry = new Entry();
    entry.setTitle("Title");
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);
    portfolio.getEntries().add(entry);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(repository.findById(1L)).thenReturn(Optional.of(portfolio));

    Entry updatedEntry = portfolioService.portfolioEntryCreation(1L, "Title");

    Assert.assertEquals(entry.getId(), updatedEntry.getId());
    Assert.assertEquals(entry.getTitle(), updatedEntry.getTitle());
    Assert.assertEquals(entry.getFields(), updatedEntry.getFields());


  }

  @Test
  void getEntryTest() {
    User user = new User();
    user.setName("studentin");
    Entry entry = new Entry();
    entry.setId(1L);
    Portfolio portfolio = new Portfolio("Lorem", user);
    portfolio.setId(1L);
    portfolio.getEntries().add(entry);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(entryRepository.save(any(Entry.class))).thenReturn(entry);
    entryRepository.save(entry);

    when(repository.findById(1L)).thenReturn(Optional.of(portfolio));

    Entry updatedEntry = portfolioService.getEntry(1L, 1L);

    Assert.assertEquals(entry, updatedEntry);


  }


  @Test
  public void findMultiplePortfoliosPerGroupTest() {
    List<Group> groupList = new ArrayList<>();

    Group group1 = new Group(0L, "Group 1", new ArrayList<>());
    Group group2 = new Group(1L, "Group 2", new ArrayList<>());
    groupList.add(group1);
    groupList.add(group2);

    List<Long> groupIds = new ArrayList<>();
    groupIds.add(0L);
    groupIds.add(1L);

    Portfolio portfolio1 = new Portfolio("Portfolio 1", group1);
    Portfolio portfolio2 = new Portfolio("Portfolio 2", group1);
    Portfolio portfolio3 = new Portfolio("Portfolio 3", group2);
    Portfolio portfolio4 = new Portfolio("Portfolio 4", group2);

    List<Portfolio> portfolioList = new ArrayList<>();
    portfolioList.add(portfolio1);
    portfolioList.add(portfolio2);
    portfolioList.add(portfolio3);
    portfolioList.add(portfolio4);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio1);
    when(repository.save(any(Portfolio.class))).thenReturn(portfolio2);
    when(repository.save(any(Portfolio.class))).thenReturn(portfolio3);
    when(repository.save(any(Portfolio.class))).thenReturn(portfolio4);

    repository.save(portfolio1);
    repository.save(portfolio2);
    repository.save(portfolio3);
    repository.save(portfolio4);

    when(repository.findAllByGroupIdIn(groupIds)).thenReturn(portfolioList);

    List<Portfolio> listOfGroupPortfolios = portfolioService.findAllByGroupList(groupList);

    assert(listOfGroupPortfolios.contains(portfolio1));
    assert(listOfGroupPortfolios.contains(portfolio2));
    assert(listOfGroupPortfolios.contains(portfolio3));
    assert(listOfGroupPortfolios.contains(portfolio4));

  }

  @Test
  public void findNoEntryByIdInEmptyPortfolioTest() {
    Portfolio testPortfolio = new Portfolio();

    Entry testEntry = portfolioService.findEntryInPortfolioById(testPortfolio, 0L);

    assert(testEntry == null);
  }

  @Test
  public void findEntryByIdTest() {
    Portfolio testPortfolio = new Portfolio();
    HashSet<Entry> entrySet = new HashSet<>();
    Entry testEntry = new Entry("test");
    testEntry.setId(7357L);
    entrySet.add(testEntry);
    testPortfolio.setEntries(entrySet);

    Entry testEntry2 = portfolioService.findEntryInPortfolioById(testPortfolio, 7357L);

    assert(testEntry2.equals(testEntry));
  }

  @Test
  public void getTemplatesOutOfMixedRepositoryTest() {
    Portfolio nonTemplate = new Portfolio("Portfolio 1", new User());
    Portfolio template = new Portfolio("Portfolio 2", new Group());

    template.setTemplate(true);

    when(repository.save(any(Portfolio.class))).thenReturn(nonTemplate);
    when(repository.save(any(Portfolio.class))).thenReturn(template);

    repository.save(nonTemplate);
    repository.save(template);

    List<Portfolio> templates = new ArrayList<>();
    templates.add(template);
    templates.add(nonTemplate);

    when(portfolioService.findAllTemplates()).thenReturn(templates);

    List<Portfolio> portfolioList = portfolioService.findAllTemplates();

    assert(portfolioList.contains(template));
    assertFalse(portfolioList.contains(nonTemplate));
  }


  @Test
  public void getNonTemplatesOutOfMixedRepositoryTest() {
    Portfolio nonTemplate = new Portfolio("Portfolio 1", new User());
    Portfolio template = new Portfolio("Portfolio 2", new Group());
    template.setTemplate(true);

    when(repository.save(any(Portfolio.class))).thenReturn(nonTemplate);
    when(repository.save(any(Portfolio.class))).thenReturn(template);

    repository.save(nonTemplate);
    repository.save(template);

    List<Portfolio> templates = new ArrayList<>();
    templates.add(template);
    templates.add(nonTemplate);

    when(portfolioService.findAllPortfolios()).thenReturn(templates);

    List<Portfolio> portfolioList = portfolioService.findAllPortfolios();

    assert(portfolioList.contains(nonTemplate));
    assertFalse(portfolioList.contains(template));

  }




}
