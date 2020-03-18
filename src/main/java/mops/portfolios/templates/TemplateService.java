package mops.portfolios.templates;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TemplateService {

    private List<Template> templateList = Arrays.asList(

        new Template("Propra1", "Übung 1", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was hast du nicht verstanden?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was könnte man besser machen?", TemplateEntry.AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", TemplateEntry.AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", TemplateEntry.AnswerType.NUMBER_SLIDER, "1,10")
        )),

        new Template("Propra1", "Übung 2", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was hast du nicht verstanden?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was könnte man besser machen?", TemplateEntry.AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", TemplateEntry.AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", TemplateEntry.AnswerType.NUMBER_SLIDER, "1,10")
        )),

        new Template("Aldat", "Vorlesung 1", Arrays.asList(
            new TemplateEntry("Was hast du heute gelernt?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was hast du nicht verstanden?", TemplateEntry.AnswerType.TEXT, ""),
            new TemplateEntry("Was könnte man besser machen?", TemplateEntry.AnswerType.MULTIPLE_CHOICE, "Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben"),
            new TemplateEntry("Schreibe eine Java-Klasse..", TemplateEntry.AnswerType.ATTACHEMENT, ".java"),
            new TemplateEntry("Bewerte die Übung heute", TemplateEntry.AnswerType.NUMBER_SLIDER, "1,10")
        ))
    );

    public List<Template> getAll() {
        return templateList;
    }

    public Template getByTitle(String title) {
        for(Template template : templateList) {
            if(template.getPortfolioTitle().equals(title)) {
                return template;
            }
        }
        return null;
    }
}
