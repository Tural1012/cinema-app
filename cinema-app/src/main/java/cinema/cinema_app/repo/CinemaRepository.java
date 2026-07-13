package cinema.cinema_app.repo;

import cinema.cinema_app.entity.Cinema;
import cinema.cinema_app.enums.MoviesType;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    @Query("SELECT DISTINCT c FROM Cinema c LEFT JOIN FETCH c.movies WHERE c.id = :id")
    Optional<Cinema> findByIdWithMovies(@Param("id") Long id);


    @Query("SELECT DISTINCT c FROM Cinema c LEFT JOIN FETCH c.movies m " +
            "WHERE c.id = :cinemaId AND m.moviesType = :type")
    Optional<Cinema> findByIdAndMovieType(@Param("cinemaId") Long id,
                                          @Param("type") MoviesType type);


    @Query("SELECT DISTINCT c FROM Cinema c LEFT JOIN FETCH c.movies")
    List<Cinema> findAllWithMovies();

    @Query("SELECT DISTINCT c FROM Cinema c LEFT JOIN FETCH c.movies WHERE c.id IN :ids")
    List<Cinema> findAllByIdWithMovies(@Param("ids") List<Long> ids);
}
