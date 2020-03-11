package mops.portfolios.Portfolio;

import lombok.*;
import mops.portfolios.Entry.Entry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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


class Test {
    static User u = new User("USER_ID_DUMMY");
    static Group g = new Group(999L);
    static Portfolio userPortfolio = new Portfolio("Title here", u);
    static Portfolio groupPortfolio = new Portfolio("Title here", g);
    static String i = userPortfolio.getUserId();
}