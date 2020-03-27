package mops.portfolios.domain.state;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StateServiceTest {

  private transient StateRepository stateRepository = mock(StateRepository.class);

  private transient StateService stateService = new StateService(stateRepository);

  @Test
  void getStateTest() {

    State state = new State();
    state.setLastState(7257L);
    state.setId("test");

    when(stateRepository.save(any(State.class))).thenReturn(state);

    stateService.setState("test", 7257L);

    when(stateRepository.findById("test")).thenReturn(Optional.of(state));

    Long stateFromRepo = stateService.getState("test");

    assert(stateFromRepo == 7257L);
  }

}
