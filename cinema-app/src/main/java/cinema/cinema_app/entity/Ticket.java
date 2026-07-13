package cinema.cinema_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "row",
                "seat_column",
                "cinema_hall_id",
                "movie_id",
                "cinema_id",
                "show_time"
        })
})
public class Ticket {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @NotNull(message = "You must select row")
            @Column(name = "row")
    Byte row;

    @NotNull(message = "You must select column")
    @Column(nullable = false, name = "seat_column")
    Byte seatColumn;

    @Email(message = "It should be real email")
    String email;

    @NotBlank(message = "Write your number")
    @Column(nullable = false)
    String number;

    @NotBlank
    @Column(nullable = false)
    @CreditCardNumber
    String creditCard;

    @Column(nullable = false)
    Boolean overEighteen;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_hall_id", nullable = false)
    CinemaHall cinemaHall;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    Movie movie;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    Double price = 0.0;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    Cinema cinema;

    @Column(nullable = false, name = "show_time")
    LocalTime showTime;
}
