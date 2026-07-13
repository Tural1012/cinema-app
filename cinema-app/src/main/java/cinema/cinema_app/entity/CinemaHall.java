package cinema.cinema_app.entity;

import cinema.cinema_app.enums.CinemaHallTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "cinema_hall")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CinemaHall {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @Enumerated(EnumType.STRING)
    CinemaHallTypes type;

    Byte hallNumber;
    @OneToMany(mappedBy = "cinemaHall",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Ticket> tickets;
}
