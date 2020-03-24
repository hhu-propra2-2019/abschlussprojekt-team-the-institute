package mops.portfolios.domain.entry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;
import mops.portfolios.domain.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
@RequiredArgsConstructor
public class EntryField {
  private @Id @GeneratedValue @Getter Long id;

  private @Setter String title;

  private @Setter String content;

  private @Setter String attachment;

  @Autowired
  private transient FileRepository fileRepository;

}
