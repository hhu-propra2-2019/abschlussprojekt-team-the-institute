package mops.portfolios;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.entry.EntryField;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.usergroup.User;
import org.springframework.stereotype.Service;

@Service
public class HardMock {
  private User getMockUser() {
    return new User("Mocked", "abc@gmail.com", null, Collections.singleton("Student"), "1");
  }

  List<Portfolio> getMockPortfolios() {
    User user = getMockUser();
    return Arrays.asList(
        new Portfolio("Machine Learning", user),
        new Portfolio("Softwareentwicklung", user)
    );
  }

  List<Portfolio> getMockGroupPortfolios() {
    User user = getMockUser();
    return Arrays.asList(
        new Portfolio("Elektronik", user),
        new Portfolio("Praktikum", user)
    );
  }

  List<Entry> getMockEntry() {
    Entry e = new Entry();
    e.setTitle("Test123");
    e.setFields(getMockEntryFields());

    Entry f = new Entry();
    f.setTitle("Test456");
    f.setFields(getMockEntryFields());

    return Arrays.asList(e, f);
  }

  private List<EntryField> getMockEntryFields() {
    EntryField first = new EntryField();
    first.setTitle("First");
    first.setContent("Lore Ipsum");
    EntryField second = new EntryField();
    second.setTitle("Second");
    second.setContent("Veni, vidi, vici");

    return Arrays.asList(first, second);
  }

  @SuppressWarnings("PMD")
  Portfolio getPortfolioByTitle(String title) {
    List<Portfolio> p = getMockPortfolios();
    List<Portfolio> q = getMockGroupPortfolios();

    Portfolio portfolio = null;

    for (Portfolio r : p) {
      if (r.getTitle().equals(title)) {
        portfolio = r;
      }
    }

    for (Portfolio r : q) {
      if (r.getTitle().equals(title)) {
        portfolio = r;
      }
    }
    return portfolio;
  }

  @SuppressWarnings("PMD")
  Entry getEntryByTitle(String title) {
    List<Entry> entries = getMockEntry();
    for (Entry e : entries) {
      if (e.getTitle().equals(title)) {
        return e;
      }
    }
    return null;
  }
}
