package cinema.cinema_app.service;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.dto.TicketDtoForUsers;
import org.springframework.cloud.client.loadbalancer.Response;

import java.util.List;
import java.util.Map;

public interface Service {
    TicketDtoForUsers createTicket(TicketDto ticket);

    List<TicketDtoForUsers> buyTickets(List<TicketDto> ticketDtoList);

    void deleteTicket(Long id);

    TicketDtoForUsers findTicketById(Long id);

    List<TicketDtoForUsers> getAllTickets();

    List<MovieDto> getAllTodayMovies();

    List<MovieDto> getAllMovies();

    List<MovieDto> getAllComingSoonMovies();

    List<TicketDtoForUsers> getPremiumTickets();

    List<TicketDtoForUsers> getStandartTickets();

    List<TicketDtoForUsers> getVipTickets();

    List<MovieDto> movieInThisCinemaById(Long id);

    List<MovieDto> getMoviesByTypeInTheCinema(String moviesType, Long id);

    List<MovieDto> getMoviesBySameNameInCinema(String name, Long id);

    Map<Byte,List<Byte>> getReservePlace(Long cinemaId, Long cinemaHall, Long movieId);

    MovieDto createMovie(MovieDto dto, Long cinemaId);

    MovieDto addMovieToCinema(Long movieId, Long cinemaId);



}
