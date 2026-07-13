package cinema.cinema_app.dto.mapper.mapperImpl;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.dto.mapper.MovieMapper;
import cinema.cinema_app.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieMapperImpl implements MovieMapper {
    @Override
    public MovieDto movieToDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setName(movie.getName());
        dto.setMoviesType(movie.getMoviesType());
        dto.setDescription(movie.getDescription());
        dto.setComingSoon(movie.getComingSoon());
        dto.setShowTime(movie.getShowTime());
        dto.setEndTime(movie.getEndTime());
        return dto;
    }

    @Override
    public Movie dtoToMovie(MovieDto dto) {
       Movie movie = new Movie();
       movie.setMoviesType(dto.getMoviesType());
       movie.setName(dto.getName());
       movie.setDescription(dto.getDescription());
       movie.setComingSoon(dto.getComingSoon());
       movie.setShowTime(dto.getShowTime());
       movie.setEndTime(dto.getEndTime());
       return movie;
    }
}
