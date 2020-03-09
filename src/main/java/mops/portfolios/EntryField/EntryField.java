package mops.portfolios.EntryField;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class EntryField {
    private @Id @GeneratedValue @Getter Long id;

    private @Setter String title;

    private @Setter String content;

    private @Setter String attachment;

}
