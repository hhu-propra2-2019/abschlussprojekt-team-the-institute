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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioServiceTest {


  private transient PortfolioRepository repository = mock(PortfolioRepository.class);
  @NonNull
  private transient PortfolioService portfolioService = new PortfolioService(repository);
  private transient EntryRepository entryRepository = mock(EntryRepository.class);
  private transient EntryFieldRepository entryFieldRepository = mock(EntryFieldRepository.class);


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
    System.out.println(portfolio);

    when(repository.save(any(Portfolio.class))).thenReturn(portfolio);
    repository.save(portfolio);

    when(repository.findById(1L)).thenReturn(Optional.of(portfolio));

    Portfolio newPortfolio = portfolioService.findPortfolioById(1L);

    Portfolio portfolioWithEntry = portfolioService.getPortfolioWithNewEntry(1L, "Lorem");

    Assert.assertEquals(newPortfolio, portfolioWithEntry);

  }

  @Test
  void getNewEntryTest() {
    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
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

    when(portfolioService.getNewEntry(1L, "Question?", portfolio)).thenReturn(entry);

    Set<EntryField> fields = portfolioService.getNewEntry(1L, "Question?", portfolio).getFields();

    System.out.println(fields);

    for (EntryField newField: fields) {
      Assert.assertEquals("EntryField(id=null, title=Question?, content=TEXT;Some hint, attachment=null)", newField.toString());
    }

  }

}
