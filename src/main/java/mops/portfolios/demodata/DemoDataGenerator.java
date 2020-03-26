package mops.portfolios.demodata;

import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.portfolio.templates.AnswerType;
import mops.portfolios.domain.user.User;

public class DemoDataGenerator {

  private final transient Faker faker = new Faker(Locale.GERMAN);

  /**
   * Generates a single EntryField.
   * @param entry - the Entry that will contain it
   * @return - the EntryField
   */
  private EntryField generateEntryField(Entry entry) {
    EntryField entryField = new EntryField();
    entryField.setAttachment(faker.shakespeare().hamletQuote());
    entryField.setContent(getRandomElement(templateEntryFieldContents));
    entryField.setTitle(faker.shakespeare().romeoAndJulietQuote());
    return entryField;
  }

  /**
   * Fills the entry with "content".
   * @param entry - the entry to fill
   * @return - the EntryFields
   */
  private Set<EntryField> generateEntryFieldSet(Entry entry) {
    return IntStream.range(0, 3).mapToObj(
        value -> generateEntryField(entry)).collect(Collectors.toSet());
  }

  /**
   * Generates an entry for a group portfolio.
   * @return - the entry
   */
  private Entry generateGroupEntry() {
    Entry entry = new Entry();
    entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
    entry.setFields(generateEntryFieldSet(entry));
    return entry;
  }

  /**
   * Generates a list of Entries for a group portfolios.
   * @return - the list
   */
  private Set<Entry> generateGroupEntrySet() {
    return IntStream.range(0, 3).mapToObj(
        value -> generateGroupEntry()).collect(Collectors.toSet());
  }

  /**
   * Generates a Group Portfolio.
   * @return - returns portfolio
   */
  public Portfolio generateGroupPortfolio() {
    Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(),generateGroup());
    portfolio.setEntries(generateGroupEntrySet());
    return portfolio;
  }

  /**
   * Generates an entry.
   * @return - returns the entry.
   */
  private Entry generateUserEntry() {
    Entry entry = new Entry();
    entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
    entry.setFields(generateEntryFieldSet(entry));
    return entry;
  }

  /**
   * Generates a list of entries.
   * @return - returns the entries
   */
  private Set<Entry> generateUserEntrySet() {
    return IntStream.range(0, 3).mapToObj(
        value -> generateUserEntry()).collect(Collectors.toSet());
  }

  /**
   * Generates a portfolio based on a user.
   * @return - return the portfolio
   */
  public Portfolio generateUserPortfolio() {
    Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(), generateUser());
    portfolio.setEntries(generateUserEntrySet());
    return portfolio;
  }

  /**
   * Generates a hard-coded user.
   * @return - returns the user
   */
  private User generateUser() {
    User user = new User();
    user.setName("studentin");
    return user;
  }

  /**
   * Generates a hard-coded group.
   * @return - returns the group
   */
  private Group generateGroup() {
    User user = new User();
    user.setName("studentin");
    User user2 = new User();
    user.setName("student");
    Group group = new Group();
    group.setTitle("Group 1");
    group.setUsers(Arrays.asList(user, user2));
    return group;
  }

  //=========================For templates

  private transient List<String> templateEntryFieldContents = Arrays.asList(
      AnswerType.TEXT.name() + ";Some hint; ",
      AnswerType.SINGLE_CHOICE.name() + ";Ja,Nein; , ",
      AnswerType.MULTIPLE_CHOICE.name()
              + ";Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben; , , ",
      AnswerType.NUMBER_SLIDER.name() + ";1,10,1; ",
      AnswerType.ATTACHEMENT.name() + ";.ascii,.pdf,.java; "
  );
  private transient List<String> templateEntryTitles = Arrays.asList(
      "Woche 1",
      "Woche 2",
      "Woche 3",
      "Woche 4",
      "Woche 5",
      "Woche 6"
  );

  private String getRandomElement(List<String> list) {
    return list.get(new Random().nextInt(list.size()));
  }

  /**
   * Generates a single template EntryField.
   * @return - the EntryField
   */
  private EntryField generateTemplateEntryField() {
    EntryField entryField = new EntryField();
    entryField.setAttachment(faker.shakespeare().hamletQuote());
    entryField.setContent(getRandomElement(templateEntryFieldContents));
    entryField.setTitle(faker.shakespeare().romeoAndJulietQuote());
    return entryField;
  }

  /**
   * Fills the template entry with "content".
   * @return - the EntryFields
   */
  public Set<EntryField> generateTemplateEntryFieldSet() {
    Set<EntryField> fields = new LinkedHashSet<>();
    for (int i = 0; i < new Random().nextInt(templateEntryFieldContents.size()); i++) {
      fields.add(generateTemplateEntryField());
    }
    return fields;
  }

  /**
   * Generates an entry for a template.
   * @return - the entry
   */
  private Entry generateTemplateEntry(int i) {
    Entry entry = new Entry();
    entry.setTitle(templateEntryTitles.get(i));
    entry.setFields(generateTemplateEntryFieldSet());

    return entry;
  }

  /**
   * Generates a set of Entries for a template.
   * @return - the list
   */
  private Set<Entry> generateTemplateEntrySet() {
    Set<Entry> entries = new LinkedHashSet<>();
    for (int i = 0; i < new Random().nextInt(templateEntryTitles.size()); i++) {
      entries.add(generateTemplateEntry(i));
    }
    return entries;

  }

  /**
   * Generates a template.
   * @return - the template
   */
  public Portfolio generateTemplate() {
    Portfolio template = new Portfolio(faker.shakespeare().asYouLikeItQuote(), generateUser());
    template.setEntries(generateTemplateEntrySet());
    template.setTemplate(true);
    return template;
  }
}
