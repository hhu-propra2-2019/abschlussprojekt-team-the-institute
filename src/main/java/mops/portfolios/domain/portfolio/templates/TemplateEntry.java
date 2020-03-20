package mops.portfolios.domain.portfolio.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class TemplateEntry {

  private long id;

  private String title;

  private Date lastChanged;

  private List<TemplateField> fields;
}
