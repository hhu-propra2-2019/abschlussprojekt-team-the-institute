package mops.portfolios.domain.usergroup;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Long groupId;
    @Column(nullable = false)
    private String groupTitle;

    public UserGroup(String userId, Long groupId, String title) {
        this.userId = userId;
        this.groupId = groupId;
        this.groupTitle = title;
    }
}
