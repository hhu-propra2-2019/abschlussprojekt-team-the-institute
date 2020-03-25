package mops.portfolios.domain.portfolio;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.entry.EntryFieldRepository;
import mops.portfolios.domain.entry.EntryRepository;
import mops.portfolios.domain.entry.EntryService;
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

public class PortfolioServiceTest {

  private transient EntryService entryService;

  private transient PortfolioService portfolioService = mock(PortfolioService.class);

  private transient EntryFieldRepository entryFieldRepository = mock(EntryFieldRepository.class);

  private transient EntryRepository entryRepository = mock(EntryRepository.class);


  @BeforeEach
  void init() {
    entryService = new EntryService(entryRepository, entryFieldRepository, portfolioService);
  }

  @SuppressWarnings("PMD")
  @Test
  void createAndAddEntryTest() {

    User user = new User();
    user.setName("studentin");
    Portfolio portfolio = new Portfolio("Lorem", user);
    Entry entry = new Entry();
    when(portfolioService.findEntryInPortfolioById(portfolio,1L)).thenReturn(entry);
    EntryField field = new EntryField();
    field.setTitle("Question?");
    field.setContent(AnswerType.TEXT + ";" + "Some hint");
    Set<EntryField> entryFields = new HashSet<>();
    entryFields.add(field);

    portfolioService.createAndAddField(portfolio, 1L, "Question?", "");

    Set<EntryField> newEntryFields = entry.getFields();

    for (EntryField newField: newEntryFields) {
      Assert.assertEquals("EntryField(id=null, title=Question?, content=TEXT;Some hint, attachment=null)", newField.toString());
    }


  }
}
