package mops.portfolios.domain.usergroup;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long>
{
    List<UserGroup> findAllByUserId(String userId);

    List<UserGroup> findAllByGroupId(Long groupId);
}
