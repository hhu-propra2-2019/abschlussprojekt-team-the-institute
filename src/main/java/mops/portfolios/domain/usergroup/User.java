package mops.portfolios.domain.usergroup;

import java.util.Set;
import lombok.Data;

@Data
public class User {
  private final String name;
  private final String email;
  private final String image;
  private final Set<String> roles;
  private final String id;

  /** All args constructor with the following variables.
   * @param name - User name
   * @param email - User email
   * @param image - User profile picture
   * @param roles - Student or Orga
   * @param id - User id
   */

  public User(String name, String email, String image, Set<String> roles, String id) {
    this.name = name;
    this.email = email;
    this.image = image;
    this.roles = roles;
    this.id = id;
  }

}