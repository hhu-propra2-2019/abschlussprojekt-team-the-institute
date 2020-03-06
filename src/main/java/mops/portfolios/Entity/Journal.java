package mops.portfolios.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Journal {
    @Id
    private int id;

    private String  title;

    private int group_id;

}
