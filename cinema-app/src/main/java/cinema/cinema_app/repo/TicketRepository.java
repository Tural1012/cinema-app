package cinema.cinema_app.repo;

import cinema.cinema_app.entity.CinemaHall;
import cinema.cinema_app.entity.Movie;
import cinema.cinema_app.entity.Ticket;
import cinema.cinema_app.enums.CinemaHallTypes;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.yaml.snakeyaml.events.Event;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT DISTINCT t FROM Ticket t " +
            "LEFT JOIN FETCH t.cinemaHall " +
            "LEFT JOIN FETCH t.movie " +
            "LEFT JOIN FETCH t.cinema " +
            "WHERE t.cinemaHall.type = :type")
    List<Ticket> findAllByCinemaHallType(@Param("type") CinemaHallTypes type);

    @Query("SELECT DISTINCT t FROM Ticket t " +
            "LEFT JOIN FETCH t.cinemaHall " +
            "LEFT JOIN FETCH t.movie " +
            "LEFT JOIN FETCH t.cinema")
    List<Ticket> findAllWithDetails();


    @Query("SELECT DISTINCT t FROM Ticket t " +
            "LEFT JOIN FETCH t.cinemaHall " +
            "LEFT JOIN FETCH t.movie " +
            "LEFT JOIN FETCH t.cinema " +
            "WHERE t.id = :id")
    Optional<Ticket> findByIdWithDetails(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tickets t USING movies m WHERE t.movie_id = m.id AND m.end_time::time < ?1", nativeQuery = true)
    void deleteTicketsByEndTime(LocalTime currentTime);

    List<Ticket> findByCinemaIdAndCinemaHallIdAndMovieId(Long cinemaId, Long cinemaHallId, Long movieId);

}
