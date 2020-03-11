package mops.portfolios.Domain.Entry;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Data
@RequiredArgsConstructor
public class Entry {
    private @Id @GeneratedValue @Getter Long id;

    private @Setter @Column(nullable = false) String title;

    private @CreatedDate Date CreatedDate;
    
    private @LastModifiedDate Date LastModifiedDate;
    
    private final UUID userId = null;

    private final Long groupId = null;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "entry",
        orphanRemoval = true
    )
    private List<EntryField> fields = new ArrayList<>();
}
