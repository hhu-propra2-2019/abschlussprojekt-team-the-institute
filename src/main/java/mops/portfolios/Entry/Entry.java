package mops.portfolios.Entry;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;

import mops.portfolios.EntryField.EntryField;

@Entity
@Data
@RequiredArgsConstructor
public class Entry {
    private @Id @GeneratedValue @Getter Long id;

    private @Setter @Column(nullable = false) String title;

    private @CreatedDate Date CreatedDate;
    
    private @LastModifiedDate Date LastModifiedDate;
    
    // @SuppressWarnings("unused")
    private final String userId;

    // @SuppressWarnings("unused")
    private final Long groupId;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<EntryField> fields = new ArrayList<>();
}
