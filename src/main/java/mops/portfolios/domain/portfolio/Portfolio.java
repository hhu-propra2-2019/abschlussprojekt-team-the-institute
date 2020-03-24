package mops.portfolios.domain.portfolio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.user.User;

@Entity
@Data
public class Portfolio {
  private @Id @GeneratedValue @Getter Long id;

  private @Column(nullable = false) @Getter @Setter String title;

  private @Getter String userId;

  private @Getter Long groupId;

  private @Setter @Getter boolean isTemplate;

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  @OrderBy("id ASC") private @Getter Set<Entry> entries = new HashSet<>();

  public Portfolio() {}

  public Portfolio(String title, User user) {
    this.title = title;
    this.userId = user.getName();
  }

  public Portfolio(String title, Group group) {
    this.title = title;
    this.groupId = group.getId();
  }
}