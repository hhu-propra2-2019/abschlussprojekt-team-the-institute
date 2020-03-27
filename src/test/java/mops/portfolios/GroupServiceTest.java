package mops.portfolios;


import lombok.NonNull;
import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.group.GroupService;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GroupServiceTest {

    @NonNull @Autowired
    private transient UserRepository userRepository = mock(UserRepository.class);
    @NonNull @Autowired
    private transient GroupRepository groupRepository = mock(GroupRepository.class);
    @NonNull @Autowired
    private transient GroupService groupService = new GroupService(groupRepository);

    @Test
    public void testTest() {
        assert(true);
    }
    @Test
    public void getUsersTest() {
        User user1 = new User("testUser", new ArrayList<>(), new ArrayList<>());

        when(userRepository.save(any(User.class))).thenReturn(user1);
        userRepository.save(user1);

        List<User> userList = new ArrayList<>();
        userList.add(user1);

        Group testGroup = new Group(0L, "testGroup", userList);

        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);
        groupService.saveGroup(testGroup);

        when(groupRepository.findById(0L)).thenReturn(Optional.of(testGroup));

        List<User> userListFromDatabase = groupService.getUsers(0L);
        System.out.println(userListFromDatabase.toString());

        assert(userListFromDatabase.containsAll(userList));
    }
}
