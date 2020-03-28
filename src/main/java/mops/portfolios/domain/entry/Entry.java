package mops.portfolios.domain.entry;

import java.util.Calendar;
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Data
@RequiredArgsConstructor
public class Entry {
  private @Id @GeneratedValue @Getter Long id;

  private @Setter @Column(nullable = false) String title;

  private final java.sql.Timestamp createdDate = new java.sql.Timestamp(
      Calendar.getInstance().getTime().getTime());

  private @LastModifiedDate Date lastModifiedDate;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true
  )
  @OrderBy("id ASC")
  private Set<EntryField> fields = new HashSet<>();

  public Entry(String title) {
    this.title = title;
  }

}
