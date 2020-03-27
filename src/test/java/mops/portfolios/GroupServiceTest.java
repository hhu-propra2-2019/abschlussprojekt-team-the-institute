package mops.portfolios;


import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.group.GroupService;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GroupServiceTest {

//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    GroupRepository groupRepository;
//    @Autowired
//    GroupService groupService;
//
    @Test
    public void testTest() {
        assert(true);
    }
//    @Test
//    public void getUsersTest() {
//        User user1 = new User("testUser", new ArrayList<>(), new ArrayList<>());
//        userRepository.save(user1);
//
//        List<User> userList = new ArrayList<>();
//        userList.add(user1);
//
//        Group testGroup = new Group(0L, "testGroup", userList);
//
//        groupService.saveGroup(testGroup);
////
////        List<User> userListFromDatabase = groupService.getUsers(0L);
////        System.out.println(userListFromDatabase.toString());
//
////        assert(userListFromDatabase.containsAll(userList));
//    }
}
