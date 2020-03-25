package mops.portfolios;

import mops.portfolios.domain.group.Group;

import mops.portfolios.domain.portfolio.Portfolio;
import mops.portfolios.domain.user.User;
import mops.portfolios.domain.user.UserRepository;
import mops.portfolios.domain.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock = mock(UserRepository.class);
    UserService userService = new UserService(userRepositoryMock);


    @Test
    public void userIsInGroupTest() {
        Group groupOfTestUser = new Group();
        List<Group> groupsOfTestUser = new ArrayList<>();
        groupsOfTestUser.add(groupOfTestUser);
        User testUser = new User("testUser", new ArrayList<>(), groupsOfTestUser);
        when(userRepositoryMock.findById("testUser")).thenReturn(Optional.of(testUser));

        assert(userService.isUserNameInGroup("testUser", groupOfTestUser));
    }


    @Test
    public void userIsNotInGroupTest() {
        Group group = new Group();
        User testUser = new User("testUser", new ArrayList<Portfolio>(), new ArrayList<Group>());
        List<User> userInList = new ArrayList<>();
        userInList.add(testUser);
        when(userRepositoryMock.findById("testUser")).thenReturn(Optional.of(testUser));

        assertFalse(userService.isUserNameInGroup("testUser", group));
    }
}
