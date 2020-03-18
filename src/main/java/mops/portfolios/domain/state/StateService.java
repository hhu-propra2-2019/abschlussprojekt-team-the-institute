package mops.portfolios.domain.state;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class StateService {

//    @PersistenceContext
//    private EntityManager em;

    StateRepository repository;

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
