package mops.portfolios.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemplateEntry {

  private String question;

  private AnswerType type;

  private String hint;
}
