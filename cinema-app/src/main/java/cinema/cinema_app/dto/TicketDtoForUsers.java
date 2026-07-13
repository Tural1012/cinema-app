package cinema.cinema_app.dto;

import cinema.cinema_app.enums.CinemaHallTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDtoForUsers implements Serializable {

    Byte row;
    Byte seatColumn;
    String email;
    String number;
    String creditCard;
    Boolean overEighteen;
    CinemaHallTypes cinemaHall;
    String movie;
    String cinema;
    LocalTime showTime;
}
