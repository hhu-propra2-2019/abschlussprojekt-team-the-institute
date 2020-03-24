package mops.portfolios.controller.services;

import lombok.AllArgsConstructor;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;

import java.util.Iterator;
import java.util.Set;

// TODO: Eventuell mit entry.EntryService zusammenführen?

@AllArgsConstructor
public class EntryService {
  private transient PortfolioService portfolioService;


  @SuppressWarnings("PMD")
  public  Entry getLast(Set<Entry> entries) {
    Iterator itr = entries.iterator();
    Entry last = (Entry)itr.next();
    while(itr.hasNext()) {
      last = (Entry)itr.next();
    }
    return last;
  }


  public void createAndAdField(Long entryId, String question, String hint, Portfolio portfolio) {
    Entry entry = portfolioService.findEntryById(portfolio, entryId);
    EntryField field = new EntryField();
    entry.getFields().add(field);

    field.setTitle(question);
    if(hint == null) {
      hint = "Some hint";
    }
    field.setContent(AnswerType.TEXT + ";" + hint);
  }

}
