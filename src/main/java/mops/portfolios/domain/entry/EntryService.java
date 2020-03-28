package mops.portfolios.domain.entry;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.controller.UserController;
import mops.portfolios.domain.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@RequiredArgsConstructor
@NoArgsConstructor
public class EntryService {

  @NonNull
  @Autowired
  transient EntryRepository entryRepository;

  @NonNull
  @Autowired
  transient EntryFieldRepository entryFieldRepository;

  @NonNull
  @Autowired
  transient PortfolioService portfolioService;

  /**
   * Finds field.
   */
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

  /**
   * Updates entryfield.
   */
  @SuppressWarnings("PMD")
  public void updateEntryFields(Long entryFieldId, String newContent, Entry entry) {
    EntryField field = findFieldById(entry, entryFieldId);
    String[] content = field.getContent().split(";");
    content[1] = newContent;
    field.setContent(content[0] + ";" + content[1] + ";" + content[2]);
    update(entry);
  }

  /**
   * Updates entryfield when checked.
   */
  @SuppressWarnings("PMD")
  public void updateEntryFieldCheck(List<String> newContent, Entry entry, EntryField field) {
    String[] content = field.getContent().split(";");
    String[] ids = content[1].split(",");
    String[] values = content[2].split(",");
    int i = 0;
    for (String id : ids) {
      if (newContent.contains(id)) {
        values[i] = "checked";
      } else {
        values[i] = "NO";
      }
      i++;
    }
    content[2] = "";
    for (String v : values) {
      content[2] += v + ",";
    }

    field.setContent(content[0] + ";" + content[1] + ";" + content[2].stripTrailing());

    update(entry);
  }

  /**
   * Updates slider entryfield.
   */
  @SuppressWarnings("PMD")
  public void updateEntryFieldSlider(String newContent, EntryField field) {
    String[] content = field.getContent().split(";");
    String[] values = content[1].split(",");
    values[2] = newContent;
    field.setContent(content[0] + ";" + values[0] + ","
        + values[1] + "," + values[2] + ";" + content[2]);
  }
}
