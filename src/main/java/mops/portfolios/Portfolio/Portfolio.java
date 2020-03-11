package mops.portfolios.Portfolio;

import jdk.internal.jline.internal.Nullable;
import lombok.*;
import mops.portfolios.Entry.Entry;
import mops.portfolios.objects.PortfolioField;

import javax.persistence.*;
import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Portfolio {
    private @Id @GeneratedValue @Getter Long id;

    private @Column(nullable = false) @Getter @Setter String title;

    private @Getter String userId;

    private @Getter Long groupId;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Entry> entries = new ArrayList<>();

    public Portfolio(){};

    public Portfolio(String title, User user)
    {
        this.title = title;
        this.userId = user.getId();
    }

    public Portfolio(String title, Group group)
    {
        this.title = title;
        this.groupId = group.getId();
    }

}

// FIXME: Dummy
class User {
    private @Getter String id;

    User(String id) {
        this.id = id;
    }
}

// FIXME: Dummy
class Group {
    private @Getter Long id;

    Group(Long id) {
        this.id = id;
    }
}

class Test {
    static User u = new User("USER_ID_DUMMY");
    static Group g = new Group(999L);
    static Portfolio userPortfolio = new Portfolio("Title here", u);
    static Portfolio groupPortfolio = new Portfolio("Title here", g);
    static String i = userPortfolio.getUserId();
}
