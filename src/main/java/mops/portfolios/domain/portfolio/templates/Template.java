package mops.portfolios.domain.portfolio.templates;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Template {

  private long id;

  private String title;

  private List<TemplateEntry> entries;

  public Date getLastChanged() {
    if(!entries.isEmpty()) {
      TemplateEntry lastEntry = entries.get(entries.size() - 1);
      return lastEntry.getLastChanged();
    }
    return null;
  }
}
