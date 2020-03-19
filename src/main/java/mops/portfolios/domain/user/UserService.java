package mops.portfolios.domain.user;

import mops.portfolios.domain.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    transient UserRepository repository;

    List<Group> getGroups(String userName) {
        return repository.findById(userName).get().getGroups();
    }
}
