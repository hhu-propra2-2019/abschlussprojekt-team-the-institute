package mops.portfolios.domain.entry;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@NoArgsConstructor
public class EntryService {

  transient @NonNull
  EntryRepository entryRepository;

  transient @NonNull
  EntryFieldRepository entryFieldRepository;

  transient @NonNull
  PortfolioService portfolioService;

  /**
   * Creates and adds an EntryField to an Entry.
   * @param entryId the id of the entry the field is to be added
   * @param question the question or title of the field to be added
   * @param hint a hint
   * @param portfolio the portfolio the entry belongs to
   */
  public void createAndAdField(Long entryId, String question, String hint, Portfolio portfolio) {
    Objects.requireNonNull(entryId);
    Entry entry = portfolioService.findEntryInPortfolioById(portfolio, entryId);
    EntryField field = new EntryField();
    entry.getFields().add(field);

    field.setTitle(question);
    if (hint == null) {
      hint = "Some hint";
    }
    field.setContent(AnswerType.TEXT + ";" + hint);
  }

  /* FIXME: DELETE IF NOT REQUIRED
   * Get the last entry in the provided Set.
   * @param entries The Set of Entries
   * @return the last entry in the provided Set
   */
  /*
  @SuppressWarnings("PMD")
  public Entry getLast(Set<Entry> entries) {
    Iterator itr = entries.iterator();
    Entry last = (Entry)itr.next();
    while (itr.hasNext()) {
      last = (Entry)itr.next();
    }
    return last;
  }*/
}
