package mops.portfolios.templates;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemplateEntry {

  private String question;

  private AnswerType type;

  private String hint;

  enum AnswerType {
    TEXT,
    SINGLE_CHOICE, //hint=choice1,choice2,...
    MULTIPLE_CHOICE, //hint=choice1,choice2,...
    NUMBER_SLIDER, //hint=min,max
    ATTACHEMENT; //hint=.asciidoc,.adoc,.yml,...
  }
}
