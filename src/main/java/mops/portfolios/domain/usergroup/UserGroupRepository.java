package mops.portfolios.domain.usergroup;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {
  List<UserGroup> findAllByUserId(String userId);

  List<UserGroup> findAllByGroupId(Long groupId);
}
