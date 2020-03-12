package mops.portfolios.Domain.Portfolio;

import lombok.*;
import mops.portfolios.Domain.Entry.*;
import mops.portfolios.Domain.UserGroup.Group;
import mops.portfolios.Domain.UserGroup.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Portfolio {
    private @Id
    @GeneratedValue
    @Getter
    Long id;

    private @Column(nullable = false)
    @Getter
    @Setter
    String title;

    private @Getter
    String userId;

    private @Getter
    Long groupId;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Entry> entries = new ArrayList<>();


    public Portfolio() {}

    public Portfolio(String title, User user) {
        this.title = title;
        this.userId = user.getId();
    }

    public Portfolio(String title, Group group) {
        this.title = title;
        this.groupId = group.getId();
    }
}