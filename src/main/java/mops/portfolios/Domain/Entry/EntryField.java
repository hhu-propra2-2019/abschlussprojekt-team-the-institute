package mops.portfolios.Domain.Entry;

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

    private @Setter String entryFieldTitle;

    private @Setter String content;

    private @Setter String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Entry entry;


}
