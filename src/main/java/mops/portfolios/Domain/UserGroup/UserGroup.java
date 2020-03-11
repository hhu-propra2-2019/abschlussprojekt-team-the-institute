package mops.portfolios.Domain.UserGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private Long groupId;
}