package mops.portfolios.domain.entry;

import lombok.NonNull;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EntryServiceTest {

  private transient @NonNull EntryService entryService;

  private transient PortfolioService portfolioService = mock(PortfolioService.class);

  private transient EntryFieldRepository entryFieldRepository = mock(EntryFieldRepository.class);

  private transient EntryRepository entryRepository = mock(EntryRepository.class);


  @BeforeEach
  void init() {
    entryService = new EntryService(entryRepository, entryFieldRepository, portfolioService);
  }

  @Test
  void createAndAddEntryTest() {

    User user = new User();
    user.setName("stduentin");

    Portfolio portfolio = new Portfolio("Lorem", user);

    Entry entry = new Entry();

    entryService.createAndAdField(1L, "Question?","", portfolio);

    when(portfolioService.findEntryInPortfolioById(portfolio,1L)).thenReturn(entry);

    System.out.println(portfolioService.findEntryInPortfolioById(portfolio,1L));

    EntryField field = new EntryField();
    field.setTitle("Question?");
    field.setContent(AnswerType.TEXT + ";" + "");
    System.out.println(field);
    Set<EntryField> entryFields = new HashSet<>();
    entryFields.add(field);

    when(entry.getFields()).thenReturn(entryFields);

    Set<EntryField> newEntryFields = entry.getFields();

    System.out.println(entryFields.toString());

    for (EntryField newField: newEntryFields) {
      Assert.assertEquals("EntryField(id=null, title=Question?, content=TEXT;, attachment=null)", newField);
    }


  }



}
