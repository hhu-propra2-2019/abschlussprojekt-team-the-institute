package mops.portfolios.domain.entry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Data
@RequiredArgsConstructor
public class EntryField {
  private @Id @GeneratedValue @Getter Long id;

  private @Setter String title;

  private @Setter String content;

  private @Setter String attachment;

}
