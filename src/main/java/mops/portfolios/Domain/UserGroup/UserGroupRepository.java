package mops.portfolios.Domain.UserGroup;

import org.springframework.data.repository.CrudRepository;
import mops.portfolios.Domain.Entry.Entry;

import java.util.List;
import java.util.UUID;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long>
{
    List<Group> findAllByUserId(UUID userId);

    List<User> findAllByGroupId(Long groupId);
}