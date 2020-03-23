package mops.portfolios.demodata;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.group.Group;
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
    entryField.setContent(faker.shakespeare().kingRichardIIIQuote());
    entryField.setTitle(faker.shakespeare().romeoAndJulietQuote());
    return entryField;
  }

  /**
   * Fills the entry with "content".
   * @param entry - the entry to fill
   * @return - the EntryFields
   */
  private List<EntryField> generateEntryFieldList(Entry entry) {
    return IntStream.range(0, 3).mapToObj(
        value -> generateEntryField(entry)).collect(Collectors.toList());
  }

  /**
   * Generates an entry for a group portfolio.
   * @return - the entry
   */
  private Entry generateGroupEntry() {
    Entry entry = new Entry();
    entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
    entry.getFields().addAll(generateEntryFieldList(entry));
    return entry;
  }

  /**
   * Generates a list of Entries for a group portfolios.
   * @return - the list
   */
  private List<Entry> generateGroupEntryList() {
    return IntStream.range(0, 3).mapToObj(
        value -> generateGroupEntry()).collect(Collectors.toList());
  }

  /**
   * Generates a Group Portfolio.
   * @return - returns portfolio
   */
  public Portfolio generateGroupPortfolio() {
    Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(),generateGroup());
    portfolio.getEntries().addAll(generateGroupEntryList());
    return portfolio;
  }

  /**
   * Generates an entry.
   * @return - returns the entry.
   */
  private Entry generateUserEntry() {
    Entry entry = new Entry();
    entry.setTitle(faker.shakespeare().romeoAndJulietQuote());
    entry.setFields(generateEntryFieldList(entry));
    return entry;
  }

  /**
   * Generates a list of entries.
   * @return - returns the entries
   */
  private List<Entry> generateUserEntryList() {
    return IntStream.range(0, 3).mapToObj(
        value -> generateUserEntry()).collect(Collectors.toList());
  }

  /**
   * Generates a portfolio based on a user.
   * @return - return the portfolio
   */
  public Portfolio generateUserPortfolio() {
    Portfolio portfolio = new Portfolio(faker.shakespeare().romeoAndJulietQuote(), generateUser());
    portfolio.getEntries().addAll(generateUserEntryList());
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
      AnswerType.TEXT.name() + ";Some hint",
      AnswerType.SINGLE_CHOICE.name() + ";Ja,Nein",
      AnswerType.MULTIPLE_CHOICE.name() + ";Mehr auf Schüler eingehen,Umfangreicher erklären,Weniger Hausaufgaben",
      AnswerType.NUMBER_SLIDER.name() + ";1,10",
      AnswerType.ATTACHEMENT.name() + ";.ascii,.pdf,.java"
  );
  private transient List<String> templateEntryFieldTitles = Arrays.asList(
      "Woche 1",
      "Woche 2",
      "Woche 3"
  );

  private String getRandomElement(List<String> list) {
    return list.get(new Random().nextInt(list.size()));
  }

  /**
   * Generates a single template EntryField.
   * @param entry - the Entry that will contain it
   * @return - the EntryField
   */
  private EntryField generateTemplateEntryField(Entry entry) {
    EntryField entryField = new EntryField();
    entryField.setAttachment(faker.shakespeare().hamletQuote());
    entryField.setContent(getRandomElement(templateEntryFieldContents));
    entryField.setTitle(faker.shakespeare().romeoAndJulietQuote());
    return entryField;
  }

  /**
   * Fills the template entry with "content".
   * @param entry - the entry to fill
   * @return - the EntryFields
   */
  private List<EntryField> generateTemplateEntryFieldList(Entry entry) {
    return IntStream.range(0, 3).mapToObj(
        value -> generateTemplateEntryField(entry)).collect(Collectors.toList());
  }

  /**
   * Generates an entry for a template.
   * @return - the entry
   */
  private Entry generateTemplateEntry(int value) {
    Entry entry = new Entry();
    entry.setTitle(templateEntryFieldTitles.get(value));
    entry.getFields().addAll(generateTemplateEntryFieldList(entry));
    return entry;
  }

  /**
   * Generates a list of Entries for a template.
   * @return - the list
   */
  private List<Entry> generateTemplateEntryList() {
    return IntStream.range(0, 3).mapToObj(
        value -> generateTemplateEntry(value)).collect(Collectors.toList());
  }

  public Portfolio generateTemplate() {
    Portfolio template = new Portfolio(faker.shakespeare().asYouLikeItQuote(), generateUser());
    template.getEntries().addAll(generateTemplateEntryList());
    template.setTemplate(true);
    return template;
  }

}
