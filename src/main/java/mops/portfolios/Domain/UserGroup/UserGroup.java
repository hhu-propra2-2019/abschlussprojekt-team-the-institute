package mops.portfolios.Domain.UserGroup;

import lombok.AllArgsConstructor;

import javax.persistence.*;
@Entity
@AllArgsConstructor
public class UserGroup {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Long groupId;
}
