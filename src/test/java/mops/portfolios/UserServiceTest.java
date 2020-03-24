package mops.portfolios;


import mops.portfolios.domain.group.Group;
import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import mops.portfolios.domain.user.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;
    UserService userService = new UserService(userRepositoryMock);

    @Before
    public void setup() {
    }


    @Test
    public void userIsInGroupTest() {
        List<Group> groupsOfTestUser = new ArrayList<>();
        Group groupOfTestUser = new Group();
        groupsOfTestUser.add(groupOfTestUser);
        User testUser = new User("testUser", new ArrayList<Portfolio>(), groupsOfTestUser);
        List<User> userInList = new ArrayList<>();
        userInList.add(testUser);
        when(userRepositoryMock.findAll()).thenReturn(userInList);

        assert(userService.isUserNameInGroup("testUser", groupOfTestUser));
    }
    @Test
    public void userIsNotInGroupTest() {

    }
    @Test
    public void userIsNotInEmptyGroupTest() {

    }
}
