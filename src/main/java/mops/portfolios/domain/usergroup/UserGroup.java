package mops.portfolios.domain.usergroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroup {
  private @Id @GeneratedValue Long id;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private Long groupId;

  @Column(nullable = false)
  private String groupTitle;

  /** All args constructor.
   *
   * @param userId - Id of the corresponding user
   * @param groupId - Id of the corresponding group
   * @param title - Name of the object
   */
  public UserGroup(String userId, Long groupId, String title) {
    this.userId = userId;
    this.groupId = groupId;
    this.groupTitle = title;
  }
}
