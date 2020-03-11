package mops.portfolios.Domain.UserGroup;

import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class User {
  private final String name;
  private final String email;
  private final String image;
  private final Set<String> roles;
  private final UUID id;

  public User(String name, String email, String image, Set<String> roles, UUID id) {
    this.name = name;
    this.email = email;
    this.image = image;
    this.roles = roles;
    this.id = id;
  }

}