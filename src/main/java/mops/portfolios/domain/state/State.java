package mops.portfolios.domain.state;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class State {
    /*
        id is the name of the system whose lastState kept e.g. "gruppenbildung"
     */
    private @Id String id;

    private @NonNull @Column(nullable = false) Long lastState;
}
