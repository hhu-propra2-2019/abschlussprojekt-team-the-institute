package mops.portfolios.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;


@Entity
@Data
@NoArgsConstructor
public class User {
  @Id String name;
  @OneToMany
  List<Portfolio> portfolios;
  @ManyToMany
  private List<Group> groups = new ArrayList<>();
}