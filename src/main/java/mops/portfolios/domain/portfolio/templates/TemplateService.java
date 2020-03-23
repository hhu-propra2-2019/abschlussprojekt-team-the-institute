package mops.portfolios.domain.portfolio.templates;

import java.util.ArrayList;
import java.util.List;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("PMD")
public class TemplateService {

  @Autowired
  private PortfolioService portfolioService;

  public List<Template> getAll() {
    List<Template> templateList = new ArrayList<>();
    for (Portfolio portfolio : portfolioService.getAllTemplates()) {
      templateList.add(convertPortfolioToTemplate(portfolio));
    }
    return templateList;
  }

  public TemplateEntry getTemplateEntryById(Template template, Long entryId) {
    for(TemplateEntry entry : template.getEntries()) {
      if(entry.getId() == entryId) {
        return entry;
      }
    }
    return null;
  }

  public Template convertPortfolioToTemplate(Portfolio portfolio) {
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
    return new Template(portfolio.getId(), portfolio.getTitle(), entries);
  }
}
