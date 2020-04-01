package mops.portfolios.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.portfolio.Portfolio;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id String name;
  @OneToMany
  List<Portfolio> portfolios;
  @ManyToMany
  private List<Group> groups = new ArrayList<>();
}