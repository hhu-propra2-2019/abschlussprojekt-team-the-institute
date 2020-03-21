package mops.portfolios.domain.portfolio.templates;

import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("PMD")
public class TemplateService {

  @Autowired
  private PortfolioService portfolioService;

  //for testing
  private List<Template> templateList = Arrays.asList(
      new Template(0, "Propra2", Arrays.asList(
          new TemplateEntry(0, "Übung1", new Date(), Arrays.asList(
              new TemplateField("Was hast du heute gelernt?", AnswerType.TEXT, "Some hint"),
              new TemplateField("Was hast du nicht verstanden?", AnswerType.TEXT, "Some hint"),
              new TemplateField("Was könnte man besser machen?", AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
              new TemplateField("Schreibe eine Java-Klasse..", AnswerType.ATTACHEMENT, ".java"),
              new TemplateField("Bewerte die Übung heute", AnswerType.NUMBER_SLIDER, "1,10")
          )),
          new TemplateEntry(1, "Übung2", new Date(), Arrays.asList(
              new TemplateField("Was hast du heute gelernt?", AnswerType.TEXT, "Some hint"),
              new TemplateField("Was hast du nicht verstanden?", AnswerType.TEXT, "Some hint"),
              new TemplateField("Was könnte man besser machen?", AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
              new TemplateField("Schreibe eine Python-Klasse..", AnswerType.ATTACHEMENT, ".py"),
              new TemplateField("Bewerte die Übung heute", AnswerType.NUMBER_SLIDER, "1,10")
          ))
      ))
  );

  //for testing
  public Template getByTitle(String portfolioTitle) {
    for (Template template : templateList) {
      if (template.getTitle().equals(portfolioTitle)) {
        return template;
      }
    }
    return null;
  }

  //for testing
  public List<Template> getAll() {
    return templateList;
  }

  //for testing
  public Template getById(long templateId) {
    for (Template template : templateList) {
      if (template.getId() == templateId) {
        return template;
      }
    }
    return null;
  }

  /*
  public List<Template> getAll() {
    return convertPortfoliosToTemplates(portfolioService.getAllPortfolios());
  }

  public Template getById(long id) {
    Portfolio portfolio = portfolioService.findPortfolioById(id);
    return convertPortfoliosToTemplates(Arrays.asList(portfolio)).get(0);
  }*/

  private List<Template> convertPortfoliosToTemplates(List<Portfolio> portfolios) {
    List<Template> templates = new ArrayList<>();
    for (Portfolio portfolio : portfolios) {
      List<TemplateEntry> entries = new ArrayList<>();
      for (Entry entry : portfolio.getEntries()) {
        List<TemplateField> fields = new ArrayList<>();
        for (EntryField field : entry.getFields()) {
          String question = field.getTitle();
          String[] answer = field.getContent().split(";");
          AnswerType type = AnswerType.valueOf(answer[0]);
          String hint = answer[1];
          fields.add(new TemplateField(question, type, hint));
        }
        entries.add(new TemplateEntry(entry.getId(), entry.getTitle(), entry.getLastModifiedDate(), fields));
      }
      templates.add(new Template(portfolio.getId(), portfolio.getTitle(), entries));
    }
    return templates;
  }
}
