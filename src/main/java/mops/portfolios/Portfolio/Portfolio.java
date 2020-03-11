package mops.portfolios.Portfolio;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mops.portfolios.Entry.Entry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Portfolio {
    private @Id @GeneratedValue @Getter Long id;

    private @Column(nullable = false) String title;

    private @Getter String userId;

    private @Getter Long groupId;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Entry> entries = new ArrayList<>();

}
