package mops.portfolios.domain.state;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StateServiceTest {

  @Autowired
  private transient StateService stateService;

  @Test
  public void getStateTest() {
    stateService.setState("test", 7257L);

    Long stateFromRepo = stateService.getState("test");

    assert(stateFromRepo == 7257L);
  }

  @Test
  public void getStateFromEmpty() {

    Long uninitializedState = stateService.getState("uninitializedState");

    assert (uninitializedState == 0L);

  }

}
