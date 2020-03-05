package mops.portfolios;

import java.text.SimpleDateFormat;
import java.util.*;
import lombok.Data;

@Data
public class Portfolio {


  private String course;

  private List<String> students = Arrays.asList("Peter", "Sarah", "Jens");

  private long lastChange = System.currentTimeMillis();

  public String getLastChangeDate() {
    Date date = new Date(lastChange);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.GERMANY);
    return dateFormat.format(date);
  }

  Portfolio(String course) {
    this.course = course;
  }
}
