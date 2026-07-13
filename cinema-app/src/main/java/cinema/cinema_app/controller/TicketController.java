package cinema.cinema_app.controller;

import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.dto.TicketDtoForUsers;
import cinema.cinema_app.service.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

public class TicketController {

    private final Service service;

    @GetMapping("/{id}")
    public TicketDtoForUsers getTicketById(@PathVariable Long id) {
        return service.findTicketById(id);
    }

    @GetMapping("/all/tickets")
    public List<TicketDtoForUsers> getAllTickets() {
        return service.getAllTickets();
    }

    @GetMapping("/premium/tickets")
    public List<TicketDtoForUsers> getPremiumTickets() {
        return service.getPremiumTickets();
    }

    @GetMapping("/standart/tickets")
    public List<TicketDtoForUsers> getStandartTickets() {
        return service.getStandartTickets();
    }

    @GetMapping("/vip/tickets")
    public List<TicketDtoForUsers> getVipTickets() {
        return service.getVipTickets();
    }

    @PostMapping("/create/ticket")
    public TicketDtoForUsers createTicket(@RequestBody @Valid TicketDto dto) {
        return service.createTicket(dto);
    }

    @PostMapping("/buy/ticket")
    public List<TicketDtoForUsers> buyTickets(@RequestBody @Valid List<TicketDto> tickets) throws InterruptedException {
        return service.buyTickets(tickets);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id);
    }

    @GetMapping("/reserve/places/in/{cinemaId}/{cinemaHall}/{movieId}")
    public Map<Byte, List<Byte>> reservedPlaces(@PathVariable Long cinemaId,
                                                @PathVariable Long cinemaHall,
                                                @PathVariable Long movieId) {
        return service.getReservePlace(cinemaId, cinemaHall, movieId);
    }


}
