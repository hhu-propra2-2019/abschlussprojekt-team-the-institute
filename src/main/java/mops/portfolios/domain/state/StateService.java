package mops.portfolios.domain.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateService {

    private transient StateRepository repository;

    @Autowired
    public StateService(StateRepository repository) {
        this.repository = repository;
    }

    public Long getState(String name) {
        Optional<State> lastState = repository.findById(name);

        if (lastState.isEmpty()) {
            return 0L;
        }

        return lastState.get().getLastState();
    }

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
