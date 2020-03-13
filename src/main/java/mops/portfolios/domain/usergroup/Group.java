package mops.portfolios.domain.usergroup;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group {
  private Long id;
  private String title;

  public Group(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public Long getId() {
    return id;
  }
}
