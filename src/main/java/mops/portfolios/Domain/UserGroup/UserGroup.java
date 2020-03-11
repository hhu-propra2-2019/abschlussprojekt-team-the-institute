package mops.portfolios.Domain.UserGroup;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class UserGroup {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Long groupId;
    @Column(nullable = false)
    private String groupTitle;
}
