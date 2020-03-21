package mops.portfolios.domain.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mops.portfolios.domain.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

  @Id
  private Long id;

  @Column(nullable = false)
  private String title;

  @ManyToMany
  private List<User> users = new ArrayList<>();


}
