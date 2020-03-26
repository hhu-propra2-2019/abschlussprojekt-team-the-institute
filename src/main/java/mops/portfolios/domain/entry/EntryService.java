package mops.portfolios.domain.entry;

import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.controller.UserController;
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

  @SuppressWarnings("PMD")
  public void updateEntryFields(RedirectAttributes redirect, @RequestParam Long entryId, @RequestParam Long entryFieldId, @RequestParam("content") String newContent, Entry entry) {
    EntryField field = findFieldById(entry, entryFieldId);
    String[] content = field.getContent().split(";");
    content[1] = newContent;
    field.setContent(content[0] + ";" + content[1] + ";" + content[2]);
    update(entry);
  }

  @SuppressWarnings("PMD")
  public void updateEntryFieldCheck(@RequestParam("button") List<String> newContent, Entry entry, EntryField field, UserController userController) {
    String[] content = field.getContent().split(";");
    String[] values = content[2].split(",");
    int i = 0;
    for(String nC : newContent) {
      if (nC.equals("checked")) {
        values[i] = nC;
      }
      i++;
    }
    content[2] = "";
    for (String v : values){
      content[2] += v + ",";
    }

    field.setContent(content[0] + ";" + content[1] + ";" + content[2].stripTrailing());

    update(entry);
  }

  @SuppressWarnings("PMD")
  public void updateEntryFieldSlider(@RequestParam("value") String newContent, EntryField field) {
    String[] content = field.getContent().split(";");
    String[] values = content[1].split(",");
    values[2] = newContent;
    field.setContent(content[0] + ";" + values[0] + "," + values[1] + "," + values[2] + ";" + content[2]);
  }
}
