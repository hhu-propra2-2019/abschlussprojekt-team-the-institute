package mops.portfolios.objects;

import lombok.Data;

@Data
public class PortfolioField {

  private String title;

  private String content;

  private String attachement;

  public PortfolioField(String title, String content, String attachement) {
    this.title = title;
    this.content = content;
    this.attachement = attachement;
  }
}