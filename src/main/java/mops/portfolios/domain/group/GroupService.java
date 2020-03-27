package mops.portfolios.domain.group;

import java.util.List;
import mops.portfolios.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

  transient GroupRepository repository;

  @Autowired
  public GroupService(GroupRepository repository) {
    this.repository = repository;
  }

  public Group getGroup(Long groupId) {
    return repository.findById(groupId).get();
  }

  /**
   * Gets list of users by groupID.
   * @return list of users
   */
  public List<User> getUsers(Long groupId) {
    Group group = repository.findById(groupId).get();

    return group.getUsers();
  }

  public void saveGroup(Group group) {
    repository.save(group);
  }

  /**
   * Updates group.
   */
  public void updateGroup(Long groupId, List<User> users) {

    Group group = repository.findById(groupId).get();

    group.setUsers(users);

    repository.save(group);
  }
}
