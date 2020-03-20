package mops.portfolios.domain.portfolio.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemplateField {

  private String question;

  private AnswerType type;

  private String hint;
}
