package mops.portfolios.domain.portfolio;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.usergroup.Group;
import mops.portfolios.domain.usergroup.User;

@Entity
@Data
public class Portfolio {
  private @Id @GeneratedValue @Getter Long id;

  private @Column(nullable = false) @Getter @Setter String title;

  private @Getter String userId;

  private @Getter Long groupId;

  private @Getter boolean isTemplate;

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  private @Getter List<Entry> entries = new ArrayList<>();


  public Portfolio() {}

  public Portfolio(String title, User user) {
    this.title = title;
    this.userId = user.getId();
  }

  public Portfolio(String title, Group group) {
    this.title = title;
    this.groupId = group.getId();
  }
}