package mops.portfolios.Domain.Portfolio;

import lombok.*;
import mops.portfolios.Domain.Entry.*;
import mops.portfolios.Domain.UserGroup.Group;
import mops.portfolios.Domain.UserGroup.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Transactional
public class Portfolio {
    private @Id
    @GeneratedValue
    Long id;

    private @Column(nullable = false)
    String title;

    private
    String userId;

    private Long groupId;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER //FIXME before prod
    )
    private List<Entry> entries = new ArrayList<>();


    public Portfolio() {
    }

    public Portfolio(String title, User user) {
        this.title = title;
        this.userId = user.getId();
    }

    public Portfolio(String title, Group group) {
        this.title = title;
        this.groupId = group.getId();
    }
}