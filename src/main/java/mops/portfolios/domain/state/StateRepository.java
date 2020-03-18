package mops.portfolios.domain.state;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface StateRepository extends CrudRepository<State, String> {
    Optional<State> findById(String id);
}

