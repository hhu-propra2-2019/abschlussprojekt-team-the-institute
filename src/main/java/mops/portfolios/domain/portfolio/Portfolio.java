package mops.portfolios.domain.portfolio;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mops.portfolios.domain.entry.Entry;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.user.User;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
public class Portfolio {
  private @Id @GeneratedValue @Getter Long id;

  private @Column(nullable = false) @Getter @Setter String title;

  private @Getter String userId;

  private @Getter Long groupId;

  private @Setter @Getter boolean isTemplate;

  private final java.sql.Timestamp createdDate = new java.sql.Timestamp(
      Calendar.getInstance().getTime().getTime());

  @OneToMany(
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  @OrderBy("id ASC")
  private @Getter Set<Entry> entries = new HashSet<>();

  public Portfolio() {}

  public Portfolio(String title, User user) {
    this.title = title;
    this.userId = user.getName();
  }

  public Portfolio(String title, Group group) {
    this.title = title;
    this.groupId = group.getId();
  }

  public long getDaysSinceCreation() {
    long difference = new Date().getTime() - getCreatedDate().getTime();
    return difference / (1000 * 60 * 60 * 24);
  }
}