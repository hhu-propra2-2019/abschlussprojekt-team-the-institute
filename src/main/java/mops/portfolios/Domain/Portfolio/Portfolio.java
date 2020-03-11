package mops.portfolios.Domain.Portfolio;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Portfolio {
    private @Id @GeneratedValue @Getter Long id;

    private @NonNull @Column(nullable = false) @Getter @Setter String title;

    private @Getter UUID accountId;

    private @Getter Long groupId;

}

class Test {
    Test() {
        Portfolio p = new Portfolio("Title", UUID.randomUUID(), null);
        Portfolio p2 = new Portfolio("Title2", null, null);
    }
}
