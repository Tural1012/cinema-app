package cinema.cinema_app.dto;

import cinema.cinema_app.entity.CinemaHall;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.enums.CinemaHallTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto implements Serializable {
    Long id;
    Byte row;
    Byte seatColumn;
    String email;
    String number;
    String creditCard;
    Boolean overEighteen;
    Long cinemaHallId;
    Long movieId;
    Long cinemaId;
    LocalDateTime createdAt;
    Double price = 0.0;

}
