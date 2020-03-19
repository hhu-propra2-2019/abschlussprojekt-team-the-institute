package mops.portfolios.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Template {

  private String portfolioTitle;

  private String entryTitle;

  private List<TemplateEntry> entries;
}
