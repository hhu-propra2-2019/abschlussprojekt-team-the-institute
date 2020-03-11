package mops.portfolios.Portfolio;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mops.portfolios.Entry.Entry;
import mops.portfolios.objects.PortfolioField;

import javax.persistence.*;
import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Portfolio {
    private @Id @GeneratedValue @Getter Long id;

    private @NonNull @Column(nullable = false) String title;

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
        this.groupId = null;
    }

    public Portfolio(String title, Group group)
    {
        this.title = title;
        this.userId = null;
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
    User u = new User("USER_ID_DUMMY");
    Group g = new Group(new Long(999));
    Portfolio userPortfolio = new Portfolio("Title here", u);
    Portfolio groupPortfolio = new Portfolio("Title here", g);
    String i = userPortfolio.getUserId();
}
