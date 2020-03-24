package mops.portfolios;


import mops.portfolios.domain.group.GroupRepository;
import mops.portfolios.domain.user.UserRepository;
import mops.portfolios.domain.user.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    GroupRepository groupRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    UserService userService;

    @Before
    public void setup() {
        this.userService =
    }


    @Test
    public void userIsInGroupTest() {
        when(groupRepositoryMock.)

    }
    @Test
    public void userIsNotInGroupTest() {

    }
    @Test
    public void userIsNotInEmptyGroupTest() {

    }
}
