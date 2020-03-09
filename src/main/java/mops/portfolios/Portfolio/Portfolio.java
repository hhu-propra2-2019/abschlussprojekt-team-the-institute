package mops.portfolios.Portfolio;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class Portfolio {
    private @Id @GeneratedValue @Getter Long id;

    private @Column(nullable = false) String title;

    private @Getter String userId;

    private @Getter Long groupId;
}
