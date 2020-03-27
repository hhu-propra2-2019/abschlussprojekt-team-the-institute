package mops.portfolios.domain.state;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private transient StateRepository repository;

    @Autowired
    public StateService(StateRepository repository) {
        this.repository = repository;
    }


  /**
   * Gets current state.
   */

  public Long getState(String name) {
    Optional<State> lastState = repository.findById(name);

    if (lastState.isEmpty()) {
      return 0L;
    }

    return lastState.get().getLastState();
  }

  /**
   * Sets new state.
   */
  public void setState(String name, Long value) {
    State lastState = repository.findById(name).orElse(null);

    if (lastState == null) {
      lastState = new State();
    }

    lastState.setLastState(value);
    lastState.setId(name);

    repository.save(lastState);
  }
}
