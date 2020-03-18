package mops.portfolios.domain.usergroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupService {

    @Autowired
    transient UserGroupRepository repository;

    public List<UserGroup> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }
}
