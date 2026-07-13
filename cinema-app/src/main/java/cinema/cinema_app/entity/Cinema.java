package cinema.cinema_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "cinema")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Column(nullable = false)
    String name;


    @OneToMany(mappedBy = "cinema",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Ticket> tickets;

    @ManyToMany(mappedBy = "cinemas", fetch = FetchType.LAZY)
    private Set<Movie> movies = new HashSet<>();
}
