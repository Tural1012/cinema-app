package cinema.cinema_app.controller;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final Service service;

    @GetMapping("/comingSoon/movies")
    public List<MovieDto> getAllComingSoonTickets() {
        return service.getAllComingSoonMovies();
    }

    @GetMapping("/today/movies")
    public List<MovieDto> getAllTodayMovies () {
        return service.getAllTodayMovies();
    }

    @GetMapping("/all/movies")
    public List<MovieDto> getAllMovies() {
        return service.getAllMovies();
    }

}
