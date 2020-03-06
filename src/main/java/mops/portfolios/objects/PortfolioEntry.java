package mops.portfolios.objects;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Data
public class PortfolioEntry {

  private int id;

  private long creationDate;

  private List<PortfolioField> fields = Arrays.asList(
      new PortfolioField("Frage1?", "Antwort1", "link"),
      new PortfolioField("Frage2?", "Antwort2", "link"),
      new PortfolioField("Frage3?", "Antwort3", "link"));

  public PortfolioEntry(int id, long creationDate) {
    this.id = id;
    this.creationDate = creationDate;
  }

  /**
   * returns creation date of the portfolio entry.
   */
  public String getCreationDateString() {
    Date date = new Date(creationDate);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.GERMANY);
    return dateFormat.format(date);
  }

  /**
   * returns days since creation of portfolio
   */
  public int getDaysSince() {
    return (int) ((System.currentTimeMillis() - creationDate) / 86400000);
  }
}
