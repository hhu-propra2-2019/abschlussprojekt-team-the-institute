package mops.portfolios.domain.portfolio;

import lombok.*;
import mops.portfolios.domain.entry.*;
import mops.portfolios.domain.usergroup.Group;
import mops.portfolios.domain.usergroup.User;

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
    String portfolioTitle;

    private @Getter
    String userId;

    private @Getter
    Long groupId;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private @Getter List<Entry> entries = new ArrayList<>();


    public Portfolio() {}

    public Portfolio(String title, User user) {
        this.portfolioTitle = title;
        this.userId = user.getId();
    }

    public Portfolio(String title, Group group) {
        this.portfolioTitle = title;
        this.groupId = group.getId();
    }
}