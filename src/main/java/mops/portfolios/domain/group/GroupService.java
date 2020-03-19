package mops.portfolios.domain.group;

import mops.portfolios.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    transient GroupRepository repository;

    public Group getGroup(Long groupId) {
        return repository.findById(groupId).get();
    }

    public List<User> getUsers(Long groupId) {
        Group group = repository.findById(groupId).get();

        return group.getUsers();
    }

    public void updateGroup(Long groupId, List<User> users) {
        Group group = repository.findById(groupId).get();

        group.setUsers(users);

        repository.save(group);
    }
}
