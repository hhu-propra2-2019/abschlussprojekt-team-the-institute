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
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.userGroup.Group;
import mops.portfolios.domain.userGroup.User;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Data
@Transactional
public class Portfolio {
  private @Id @GeneratedValue Long id;

  private @Column(nullable = false) String title;

  private String userId;

  private Long groupId;

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER //FIXME before prod
  )
  private List<Entry> entries = new ArrayList<>();


  public Portfolio() {
  }

  public Portfolio(String title, User user) {
    this.title = title;
    this.userId = user.getId();
  }

  public Portfolio(String title, Group group) {
    this.title = title;
    this.groupId = group.getId();
  }
}