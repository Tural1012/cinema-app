package cinema.cinema_app.service.serviceImpl;

import cinema.cinema_app.dto.MovieDto;
import cinema.cinema_app.dto.TicketDto;
import cinema.cinema_app.dto.TicketDtoForUsers;
import cinema.cinema_app.dto.mapper.MovieMapper;
import cinema.cinema_app.dto.mapper.TicketMapper;
import cinema.cinema_app.entity.*;
import cinema.cinema_app.enums.CinemaHallTypes;

import cinema.cinema_app.repo.CinemaHallRepository;
import cinema.cinema_app.repo.CinemaRepository;
import cinema.cinema_app.repo.MovieRepository;
import cinema.cinema_app.repo.TicketRepository;
import cinema.cinema_app.exception.SeatAlreadyTakenException;
import cinema.cinema_app.service.EmailService;
import cinema.cinema_app.service.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceImpl implements Service {

    private final TicketRepository ticketRepository;

    private final MovieRepository movieRepository;

    private final CinemaHallRepository cinemaHallRepository;

    private final CinemaRepository cinemaRepository;

    private final TicketMapper ticketMapper;

    private final MovieMapper movieMapper;

    private final EmailService emailService;


    @Override
    @Transactional
    //@CacheEvict(value = {"allTickets", "premiumTickets", "standartTickets", "vipTickets", "reservedPlaces"}, allEntries = true)
    public TicketDtoForUsers createTicket(TicketDto ticket) {
        Long movieId = ticket.getMovieId();
        Long cinemaHallId = ticket.getCinemaHallId();
        Long cinemaId = ticket.getCinemaId();

        Movie movie1 = movieRepository.findById(movieId)
                .orElseThrow(() -> new NullPointerException("Movie not found with id: " + movieId));


        CinemaHall cinemaHall = cinemaHallRepository.findById(cinemaHallId)
                .orElseThrow(() -> new NullPointerException("Cinema hall not found with id: " + cinemaHallId));

        Cinema cinema = cinemaRepository.findByIdWithMovies(cinemaId)
                .orElseThrow(() -> new NullPointerException("Cinema not found with id: " + cinemaId));
       if (!cinema.getMovies().contains(movie1)){
           throw new RuntimeException(movie1.getName() + " is not showing in " + cinema.getName());
       }
        Ticket newticket = ticketMapper.toEntityWithReferences(ticket, movie1, cinemaHall, cinema);

        if (cinemaHall.getType().equals(CinemaHallTypes.Premium)){
            newticket.setPrice(PremiumCinemaHall.price);
        } else if (cinemaHall.getType().equals(CinemaHallTypes.Standart)) {
            newticket.setPrice(StandartCinemaHall.price);
        }
        else {
            newticket.setPrice(VipCinemaHall.price);
        }
        try {
            ticketRepository.saveAndFlush(newticket);
        } catch (DataIntegrityViolationException e) {
            throw new SeatAlreadyTakenException(
                    "Seat " + newticket.getRow() + "/" + newticket.getSeatColumn()
                            + " for " + movie1.getName() + " is already booked");
        }
        return ticketMapper.dtoForUser(newticket);
    }



    @Override
    @Transactional
    @CachePut(value = {"allTickets", "premiumTickets", "standartTickets", "vipTickets", "reservedPlaces"})
    public List<TicketDtoForUsers> buyTickets(List<TicketDto> ticketDtoList) {
        if (ticketDtoList == null || ticketDtoList.isEmpty()) {
            throw new IllegalArgumentException("Ticket list cannot be empty");
        }

        List<Long> movieIds = ticketDtoList.stream()
                .map(TicketDto::getMovieId)
                .distinct()
                .toList();

        List<Long> cinemaHallIds = ticketDtoList.stream()
                .map(TicketDto::getCinemaHallId)
                .distinct()
                .toList();

        List<Long> cinemaIds = ticketDtoList.stream()
                .map(TicketDto::getCinemaId)
                .distinct()
                .toList();

        Map<Long, Movie> moviesMap = movieRepository.findAllById(movieIds).stream()
                .collect(Collectors.toMap(Movie::getId, Function.identity()));

        Map<Long, CinemaHall> cinemaHallsMap = cinemaHallRepository.findAllById(cinemaHallIds).stream()
                .collect(Collectors.toMap(CinemaHall::getId, Function.identity()));

        Map<Long, Cinema> cinemasMap = cinemaRepository.findAllByIdWithMovies(cinemaIds).stream()
                .collect(Collectors.toMap(Cinema::getId, Function.identity()));

        List<TicketDtoForUsers> ticketDtoForUsersList = new LinkedList<>();

        for (TicketDto ticketDto : ticketDtoList) {
            Long movieId = ticketDto.getMovieId();
            Long cinemaHallId = ticketDto.getCinemaHallId();
            Long cinemaId = ticketDto.getCinemaId();


            Movie movie = moviesMap.get(movieId);
            if (movie == null) {
                throw new EntityNotFoundException("Movie not found with id: " + movieId);
            }

            if (movie.getShowTime().isBefore(now().toLocalTime())){
                throw new RuntimeException("Movie has been started");
            }

            CinemaHall cinemaHall = cinemaHallsMap.get(cinemaHallId);
            if (cinemaHall == null) {
                throw new EntityNotFoundException("Cinema hall not found with id: " + cinemaHallId);
            }

            Cinema cinema = cinemasMap.get(cinemaId);
            if (cinema == null) {
                throw new EntityNotFoundException("Cinema not found with id: " + cinemaId);
            }

            if (!cinema.getMovies().contains(movie)) {
                throw new RuntimeException(movie.getName() + " is not showing in " + cinema.getName());
            }

            Ticket newTicket = ticketMapper.toEntityWithReferences(ticketDto, movie, cinemaHall, cinema);

            switch (cinemaHall.getType()) {
                case Premium -> newTicket.setPrice(PremiumCinemaHall.price);
                case Standart -> newTicket.setPrice(StandartCinemaHall.price);
                case VIP -> newTicket.setPrice(VipCinemaHall.price);
            }

            try {
                ticketRepository.saveAndFlush(newTicket);
            } catch (DataIntegrityViolationException e) {
                throw new SeatAlreadyTakenException(
                        "Seat " + newTicket.getRow() + "/" + newTicket.getSeatColumn()
                                + " for " + movie.getName() + " is already booked");
            }

            ticketDtoForUsersList.add(ticketMapper.dtoForUser(newTicket));
        }


        sendTicketEmailsAfterCommit(ticketDtoForUsersList);

        return ticketDtoForUsersList;
    }

    private void sendTicketEmailsAfterCommit(List<TicketDtoForUsers> tickets) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    tickets.forEach(emailService::mailSender);
                }
            });
        } else {
            tickets.forEach(emailService::mailSender);
        }
    }


    @Override
    @Transactional
   // @CacheEvict(value = {"allTickets", "premiumTickets", "standartTickets", "vipTickets", "reservedPlaces", "tickets"}, key = "#id")
    public void deleteTicket(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        ticketRepository.delete(ticket.get());
    }

    @Override
   // @Cacheable(value = "tickets", key = "#id")
    public TicketDtoForUsers  findTicketById(Long id) {
      Optional<Ticket> ticket = ticketRepository.findByIdWithDetails(id);
        if (ticket.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return ticketMapper.dtoForUser(ticket.get());
    }

    @Override
   // @Cacheable(value = "allTickets", key = "'allTickets'", unless = "#result.isEmpty()")
    public List<TicketDtoForUsers> getAllTickets() {
        return ticketRepository.findAllWithDetails()
                .stream()
                .map(ticketMapper::dtoForUser)
                .toList();

    }

    @Override
    //@Cacheable(value = "todayMovies", key = "'todayMovies'", unless = "#result.isEmpty()")
    public List<MovieDto> getAllTodayMovies() {
        return movieRepository.findAllTodayMoviesWithCinemas()
                .stream()
                .filter(movie -> movie.getShowTime().isAfter(now().toLocalTime()))
                .map(movieMapper::movieToDto)
                .toList();
    }

    @Override
    @Cacheable(value = "allMovies", key = "'allMovies'", unless = "#result.isEmpty()")
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAllTodayMoviesWithCinemas()
                .stream()
                .map(movieMapper::movieToDto)
                .toList();
    }

    @Override
    @Cacheable(value = "comingSoonMovies", key = "'comingSoonMovies'", unless = "#result.isEmpty()")
    public List<MovieDto> getAllComingSoonMovies() {
        return movieRepository.findAllComingSoonWithCinemas()
                .stream()
                .map(movieMapper::movieToDto)
                .toList();
    }

    @Override
   // @Cacheable(value = "premiumTickets", key = "'premiumTickets'", unless = "#result.isEmpty()")
    public List<TicketDtoForUsers> getPremiumTickets() {
        return ticketRepository.findAllByCinemaHallType(CinemaHallTypes.Premium)
                .stream()
                .map(ticketMapper::dtoForUser)
                .toList();
    }

    @Override
  //  @Cacheable(value = "standartTickets", key = "'standartTickets'", unless = "#result.isEmpty()")
    public List<TicketDtoForUsers> getStandartTickets() {
        return ticketRepository.findAllByCinemaHallType(CinemaHallTypes.Standart)
                .stream()
                .map(ticketMapper::dtoForUser)
                .toList();
    }

    @Override
  //  @Cacheable(value = "vipTickets", key = "'vipTickets'", unless = "#result.isEmpty()")
    public List<TicketDtoForUsers> getVipTickets() {
        return ticketRepository.findAllByCinemaHallType(CinemaHallTypes.VIP)
                .stream()
                .map(ticketMapper::dtoForUser)
                .toList();
    }

    @Override
    @Cacheable(value = "cinemaMovies", key = "#id")
    public List<MovieDto> movieInThisCinemaById(Long id) {
        Cinema cinema = cinemaRepository.findByIdWithMovies(id).orElseThrow(() -> new RuntimeException("Cinema not found with id " + id));
        return cinema.getMovies().stream().map(movieMapper::movieToDto).toList();
    }

    @Override
    @Cacheable(value = "cinemaMoviesByType", key = "#moviesType + '_' + #id", unless = "#result.isEmpty()")
    public List<MovieDto> getMoviesByTypeInTheCinema(String moviesType, Long id) {
        Cinema cinema = cinemaRepository.findByIdWithMovies(id).orElseThrow(() -> new RuntimeException("Cinema not found with id " + id));
        return cinema.getMovies().stream()
                .filter(movie -> movie.getMoviesType().name().equalsIgnoreCase(moviesType))
                .map(movieMapper::movieToDto).toList();
    }

    @Override
    @Cacheable(value = "cinemaMoviesByName", key = "#name + '_' + #id", unless = "#result.isEmpty()")
    public List<MovieDto> getMoviesBySameNameInCinema(String name, Long id) {
        List<Movie> d = movieRepository.findMoviesByNameAndCinemaId(name, id);
        return d.stream().map(movieMapper::movieToDto).toList();
    }

    @Override
    @CachePut(value = "reservedPlaces", key = "#cinemaId + '_' + #cinemaHallId + '_' + #movieId")
    public Map<Byte,List<Byte>>  getReservePlace(Long cinemaId, Long cinemaHallId, Long movieId) {

        Map<Byte, List<Byte>> reservePlaces = new HashMap<>();
        List<Ticket> tickets = ticketRepository.findByCinemaIdAndCinemaHallIdAndMovieId(cinemaId, cinemaHallId, movieId);

        for (Ticket ticket : tickets) {
            Byte row = ticket.getRow();
            Byte seatColumn = ticket.getSeatColumn();

            reservePlaces.computeIfAbsent(row, k -> new ArrayList<>()).add(seatColumn);
        }

        return reservePlaces;
    }



    @Override
    @Transactional
    @CacheEvict(value = {"allMovies", "todayMovies", "comingSoonMovies", "cinemaMovies",
            "cinemaMoviesByType", "cinemaMoviesByName"}, allEntries = true)
    public MovieDto createMovie (MovieDto movieDto, Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new EntityNotFoundException("Cinema not found"));
        Movie movie = movieMapper.dtoToMovie(movieDto);
        movie.getCinemas().add(cinema);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.movieToDto(savedMovie);
    }

    @Override
    @CacheEvict(value = {"allMovies", "todayMovies", "comingSoonMovies",
            "cinemaMovies", "cinemaMoviesByType", "cinemaMoviesByName"}, allEntries = true)
    public MovieDto addMovieToCinema(Long movieId, Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new EntityNotFoundException("Cinema not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new EntityNotFoundException("Movie not Found"));

        movie.getCinemas().add(cinema);
        movieRepository.save(movie);
        return movieMapper.movieToDto(movie);

    }


}
