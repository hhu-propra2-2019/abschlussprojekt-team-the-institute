package mops.portfolios.domain.user;

import mops.portfolios.domain.group.Group;
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
  private transient UserRepository userRepositoryMock = mock(UserRepository.class);
  private transient UserService userService = new UserService(userRepositoryMock);
  private transient final String userName = "testUser";

  @Test
  public void userIsInGroupTest() {
    Group groupOfTestUser = new Group();
    List<Group> groupsOfTestUser = new ArrayList<>();
    groupsOfTestUser.add(groupOfTestUser);
    User testUser = new User(userName, new ArrayList<>(), groupsOfTestUser);
    when(userRepositoryMock.findById(userName)).thenReturn(Optional.of(testUser));

    assert(userService.isUserNameInGroup(userName, groupOfTestUser));
  }


  @Test
  public void userIsNotInGroupTest() {
    Group group = new Group();
    User testUser = new User(userName, new ArrayList<>(), new ArrayList<>());
    List<User> userInList = new ArrayList<>();
    userInList.add(testUser);
    when(userRepositoryMock.findById(userName)).thenReturn(Optional.of(testUser));

    assertFalse(userService.isUserNameInGroup(userName, group));
  }
}
