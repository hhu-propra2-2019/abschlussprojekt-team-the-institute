package mops.portfolios.domain.user;

import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    transient UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<Group> getGroupsByUserName(String userName) {
        User user = repository.findOneByName(userName);
        if (null == user) return new ArrayList<>();
        return user.getGroups();
    }

    public boolean isUserNameInGroup(String userName, Group group) {
        List<Group> groups = repository.findById(userName).get().getGroups();
        return groups.contains(group);
    }
}
