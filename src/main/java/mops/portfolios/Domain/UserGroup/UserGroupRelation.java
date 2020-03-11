package mops.portfolios.Domain.UserGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
public class UserGroupRelation {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    final private UUID userId;
    @Column(nullable = false)
    final private Long groupId;
}