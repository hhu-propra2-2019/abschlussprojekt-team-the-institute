package mops.portfolios.demodata;

import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.usergroup.Group;
import mops.portfolios.domain.usergroup.User;

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
    HashSet<String> roles = new HashSet<>();
    roles.add("role_1");
    roles.add("role_2");
    roles.add("role_3");
    return new User(
        "user_name",
        "user_mail",
        "user_image",
        roles,
        "userId");
  }

  /**
   * Generates a hard-coded group.
   * @return - returns the group
   */
  private Group generateGroup() {
    return new Group(9876543210L, "test");
  }

  public Portfolio generateTemplate() {
    Portfolio template = new Portfolio(faker.shakespeare().asYouLikeItQuote(), generateUser());
    template.getEntries().addAll(generateUserEntryList());
    template.setTemplate(true);
    return template;
  }

}
