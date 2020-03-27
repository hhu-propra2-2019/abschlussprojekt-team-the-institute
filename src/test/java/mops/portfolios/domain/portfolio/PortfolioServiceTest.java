package mops.portfolios.domain.portfolio;

import lombok.NonNull;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PortfolioServiceTest {


  private transient PortfolioRepository repository = mock(PortfolioRepository.class);
  @NonNull
  private transient PortfolioService portfolioService = new PortfolioService(repository);
  private transient EntryRepository entryRepository = mock(EntryRepository.class);
  private transient EntryFieldRepository entryFieldRepository = mock(EntryFieldRepository.class);
  private transient KeycloakAuthenticationToken token = mock(KeycloakAuthenticationToken.class);


  @SuppressWarnings("PMD")
  @Test
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

    portfolioService.createAndAddField(portfolio, 1L, "Question?", "");

    Set<EntryField> newEntryFields = entry.getFields();

    for (EntryField newField: newEntryFields) {
      Assert.assertEquals("EntryField(id=1, title=Question?, content=TEXT;Some hint, attachment=null)", newField.toString());
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

}
