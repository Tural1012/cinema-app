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

    @GetMapping("/movie/comingSoon")
    public List<MovieDto> getAllComingSoonTickets() {
        return service.getAllComingSoonMovies();
    }

    @GetMapping("/movie/today")
    public List<MovieDto> getAllTodayMovies () {
        return service.getAllTodayMovies();
    }

    @GetMapping("/movie/all")
    public List<MovieDto> getAllMovies() {
        return service.getAllMovies();
    }

}
