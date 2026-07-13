package cinema.cinema_app.dto.mapper.mapperImpl;

import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.dto.TicketDtoForUsers;
import cinema.cinema_app.dto.mapper.TicketMapper;
import cinema.cinema_app.entity.Cinema;
import cinema.cinema_app.entity.CinemaHall;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketMapperImpl implements TicketMapper {

    @Override
    public Ticket dtoToTicket(TicketDto dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setRow(dto.getRow());
        ticket.setSeatColumn(dto.getSeatColumn());
        ticket.setEmail(dto.getEmail());
        ticket.setNumber(dto.getNumber());
        ticket.setCreditCard(dto.getCreditCard());
        ticket.setOverEighteen(dto.getOverEighteen());
        ticket.setCreatedAt(dto.getCreatedAt());
        ticket.setPrice(dto.getPrice());
        return ticket;
    }
    public Ticket toEntityWithReferences(TicketDto dto, Movie movie, CinemaHall cinemaHall, Cinema cinema) {
        Ticket ticket = dtoToTicket(dto);
        ticket.setMovie(movie);
        ticket.setCinemaHall(cinemaHall);
        ticket.setCinema(cinema);
        ticket.setShowTime(movie.getShowTime());
        return ticket;
    }
    @Override
    public TicketDto ticketToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setRow(ticket.getRow());
        dto.setSeatColumn(ticket.getSeatColumn());
        dto.setEmail(ticket.getEmail());
        dto.setNumber(ticket.getNumber());
        dto.setCreditCard(ticket.getCreditCard());
        dto.setOverEighteen(ticket.getOverEighteen());
        dto.setCinemaHallId(ticket.getCinemaHall().getId());
        dto.setMovieId(ticket.getMovie().getId());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setPrice(ticket.getPrice());
        return dto;
    }




    @Override
    public TicketDtoForUsers dtoForUser(Ticket ticket) {
        TicketDtoForUsers ticketDtoForUsers = new TicketDtoForUsers();
        ticketDtoForUsers.setRow(ticket.getRow());
        ticketDtoForUsers.setSeatColumn(ticket.getSeatColumn());
        ticketDtoForUsers.setEmail(ticket.getEmail());
        ticketDtoForUsers.setCreditCard(ticket.getCreditCard());
        ticketDtoForUsers.setShowTime(ticket.getShowTime());
        ticketDtoForUsers.setNumber(ticket.getNumber());
        ticketDtoForUsers.setOverEighteen(ticket.getOverEighteen());
        ticketDtoForUsers.setMovie(ticket.getMovie().getName());
        ticketDtoForUsers.setCinema(ticket.getCinema().getName());
        ticketDtoForUsers.setCinemaHall(ticket.getCinemaHall().getType());
        return ticketDtoForUsers;
    }
}
