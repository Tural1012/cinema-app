package cinema.cinema_app.controller;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.service.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CinemaMoviesController {

    private final Service service;

    @GetMapping("/cinema/movies/{id}")
    public List<MovieDto> cinemaMovies(@PathVariable Long id) {
        return service.movieInThisCinemaById(id);
    }

    @GetMapping("/cinema/movies/by/type/{type}/{id}")
    public List<MovieDto> cinemaMoviesByMoviesTypes(@PathVariable String type, @PathVariable Long id){
        return service.getMoviesByTypeInTheCinema(type, id);
    }

    @GetMapping("/movies/{name}/cinema/{id}")
    public List<MovieDto> findSameMoviesInCinema(@PathVariable String name, @PathVariable Long id) {
        return service.getMoviesBySameNameInCinema(name, id);
    }


    @PostMapping("/create/movie/in/{cinemaId}")
    public ResponseEntity<MovieDto> createMovie(@RequestBody @Valid MovieDto dto, @PathVariable Long cinemaId){
        MovieDto createdMovie = service.createMovie(dto, cinemaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PostMapping("/add/movie/{movieId}/to/{cinemaId}")
    public ResponseEntity<MovieDto> addMovieToCinemaWithID(@PathVariable Long movieId,
                                                           @PathVariable Long cinemaId){
        MovieDto addedMovie = service.addMovieToCinema(movieId, cinemaId);
        return ResponseEntity.status(HttpStatus.OK).body(addedMovie);
    }


}
