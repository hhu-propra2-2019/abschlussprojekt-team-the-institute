package mops.portfolios.domain.portfolio.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Template {

  private long id;

  private String title;

  private List<TemplateEntry> entries;

  public TemplateEntry getLastEntry() {
    return entries.get(entries.size() - 1);
  }
}