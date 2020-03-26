package mops.portfolios.domain.entry;

import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@RequiredArgsConstructor
@NoArgsConstructor
public class EntryService {

  @NonNull
  transient EntryRepository entryRepository;

  @NonNull
  transient EntryFieldRepository entryFieldRepository;

  @NonNull
  transient PortfolioService portfolioService;

  @SuppressWarnings("PMD")
  public EntryField findFieldById(Entry entry, Long entryFieldId) {
    for (EntryField field : entry.getFields()) {
      if (field.getId() == entryFieldId) {
        return field;
      }
    }
    return null;
  }

  public void update(Entry entry) {
    entryRepository.save(entry);
  }
  
  public void updateEntryFields(RedirectAttributes redirect, @RequestParam Long entryId, @RequestParam Long entryFieldId, @RequestParam("content") String newContent, Entry entry) {
    EntryField field = findFieldById(entry, entryFieldId);

    field.setContent(newContent);
    update(entry);

  }
  
}
