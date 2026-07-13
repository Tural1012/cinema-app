package cinema.cinema_app.dto.mapper;

import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.dto.TicketDtoForUsers;
import cinema.cinema_app.entity.Cinema;
import cinema.cinema_app.entity.CinemaHall;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.entity.Ticket;

public interface TicketMapper {
    TicketDto ticketToDto(Ticket ticket);

    Ticket dtoToTicket(TicketDto dto);

    Ticket toEntityWithReferences(TicketDto dto, Movie movie, CinemaHall cinemaHall, Cinema cinema);

    TicketDtoForUsers dtoForUser(Ticket dto);
}
