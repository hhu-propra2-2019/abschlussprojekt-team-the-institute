package mops.portfolios.domain.entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
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
      fetch = FetchType.EAGER, //FIXME
      orphanRemoval = true
  )
  private List<EntryField> fields = new ArrayList<>();

  public Entry(String title) {
    this.title = title;
  }

}
