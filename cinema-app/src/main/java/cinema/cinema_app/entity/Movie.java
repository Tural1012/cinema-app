package cinema.cinema_app.entity;

import cinema.cinema_app.enums.MoviesType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "movies")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    Long id;
     @NotBlank
    String name;

    String description;
    @Enumerated(EnumType.STRING)
    MoviesType moviesType;

    Boolean comingSoon;

    @OneToMany(mappedBy = "movie",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Ticket> tickets;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_cinema",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "cinema_id")
    )
    private Set<Cinema> cinemas = new HashSet<>();

    @Column(nullable = false)
    LocalTime showTime;

    @Column
    LocalTime endTime;
}
