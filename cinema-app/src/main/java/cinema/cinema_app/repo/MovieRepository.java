package cinema.cinema_app.repo;

import cinema.cinema_app.entity.Movie;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN FETCH m.cinemas " +
            "WHERE m.comingSoon = false")
    List<Movie> findAllTodayMoviesWithCinemas();

    @Query("SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN FETCH m.cinemas " +
            "WHERE m.comingSoon = true")
    List<Movie> findAllComingSoonWithCinemas();

    @Query("SELECT m FROM Movie m JOIN m.cinemas c WHERE m.name = :name AND c.id = :cinemaId")
    List<Movie> findMoviesByNameAndCinemaId(@Param("name") String name, @Param("cinemaId") Long cinemaId);
}
