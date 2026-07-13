package cinema.cinema_app.service;

import cinema.cinema_app.dto.TicketDtoForUsers;

public interface EmailService {
    public void mailSender(TicketDtoForUsers dto);
    public String buildHTMLTicket(TicketDtoForUsers dto);
}
