package cinema.cinema_app.dto.mapper;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.entity.CinemaHall;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.entity.Ticket;

public interface MovieMapper {
    MovieDto movieToDto(Movie movie);

    Movie dtoToMovie(MovieDto dto);

}
