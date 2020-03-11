package mops.portfolios.Domain.UserGroup;

import lombok.Data;

@Data
public class Group {
    private Long id;
    private String title;

    Group(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
