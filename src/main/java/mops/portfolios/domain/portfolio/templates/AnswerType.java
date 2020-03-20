package mops.portfolios.domain.portfolio.templates;

public enum AnswerType {
  TEXT,
  SINGLE_CHOICE, //hint=choice1,choice2,...
  MULTIPLE_CHOICE, //hint=choice1,choice2,...
  NUMBER_SLIDER, //hint=min,max
  ATTACHEMENT; //hint=.asciidoc,.adoc,.yml,...
}
