package mops.portfolios.domain.user;

import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    transient UserRepository repository;

    @Autowired
    transient GroupRepository groupRepository;

    public User findByUserName(String userName) {
        return repository.findById(userName).get();
    }

    public List<Group> getGroupsByUser(User user) {
        return repository.findById(user.getName()).get().getGroups();
    }

    public boolean isUserNameInGroup(String userName, Group group) {
        List<Group> groups = repository.findById(userName).get().getGroups();
        return groups.contains(group);
    }
}
