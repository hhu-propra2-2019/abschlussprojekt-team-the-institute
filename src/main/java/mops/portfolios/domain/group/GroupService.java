package mops.portfolios.domain.group;

import mops.portfolios.domain.usergroup.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Optional<Group> findById(Long id);

    Group create(List<User> users);

    void  deleteById(Long id);
}
