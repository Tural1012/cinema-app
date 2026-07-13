package cinema.cinema_app.service;

import cinema.cinema_app.repo.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCleanupService {

    private final TicketRepository ticketRepository;


    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupExpiredTickets() {
        LocalTime now = LocalTime.now();
        log.info("Cleaning is starting");
        ticketRepository.deleteTicketsByEndTime(now);
    }
}