package mops.portfolios.objects;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class Portfolio {

  private String title; //course?

  private List<String> groupStudents = Arrays.asList("Peter", "Sarah", "Jens");

  private List<PortfolioEntry> entries = Arrays.asList(
      new PortfolioEntry(2, System.currentTimeMillis()),
      new PortfolioEntry(1, System.currentTimeMillis() - 604800000));

  public Portfolio(String title) {
    this.title = title;
  }
}
