package mops.portfolios.domain.entry;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Data
@RequiredArgsConstructor
public class Entry {
    private @Id @GeneratedValue @Getter Long id;

    private @Setter
    @Column(nullable = false) String title;

    private final java.sql.Timestamp CreatedDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    
    private @LastModifiedDate Date LastModifiedDate;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, //FIXME
        orphanRemoval = true
    )
    private List<EntryField> fields = new ArrayList<>();

    public Entry(String title) {
        this.title = title;
    }

}
