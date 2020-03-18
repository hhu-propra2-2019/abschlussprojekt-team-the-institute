package mops.portfolios.domain.usergroup;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService {

  @Autowired
  transient UserGroupRepository repository;

  public List<UserGroup> findAllByUserId(String userId) {
    return repository.findAllByUserId(userId);
  }
}
