package mops.portfolios.Domain.UserGroup;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long>
{
    List<Group> findAllByUserId(String userId);

    List<User> findAllByGroupId(Long groupId);
}
