package mops.portfolios.templates;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TemplateService {

    private List<Template> templateList = Arrays.asList(

        new Template("Propra1", "Übung_1", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was hast du nicht verstanden?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was könnte man besser machen?", AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", AnswerType.NUMBER_SLIDER, "1,10")
        )),

        new Template("Propra1", "Übung_2", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was hast du nicht verstanden?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was könnte man besser machen?", AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", AnswerType.NUMBER_SLIDER, "1,10")
        )),

        new Template("Aldat", "Vorlesung_1", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was hast du nicht verstanden?", AnswerType.TEXT, "Some hint"),
            new TemplateEntry("Was könnte man besser machen?", AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", AnswerType.NUMBER_SLIDER, "1,10")
        ))
    );

    public List<Template> getAll() {
        return templateList;
    }

    public Template get(String portfolioTitle, String entryTitle) {
        for(Template template : templateList) {
            if(template.getPortfolioTitle().equals(portfolioTitle) && template.getEntryTitle().equals(entryTitle)) {
                return template;
            }
        }
        return null;
    }
}
