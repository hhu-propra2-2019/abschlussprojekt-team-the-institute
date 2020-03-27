package mops.portfolios.domain.group;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.portfolios.domain.user.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_table")
public class Group {

  @Id
  private Long id;

  @Column(nullable = false)
  private String title;

  @ManyToMany
  private List<User> users = new ArrayList<>();


}
