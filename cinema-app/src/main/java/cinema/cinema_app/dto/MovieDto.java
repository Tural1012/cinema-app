package cinema.cinema_app.dto;

import cinema.cinema_app.enums.MoviesType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto implements Serializable {
    String name;
    String description;
    MoviesType moviesType;
    Boolean comingSoon;
    LocalTime showTime;
    LocalTime endTime;
}
