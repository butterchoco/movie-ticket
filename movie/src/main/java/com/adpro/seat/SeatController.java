package com.adpro.seat;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeatController {
    private TheatreRepository theatreRepository;
    private MovieSessionRepository movieSessionRepository;
    private MovieRepository movieRepository;

    @Autowired
    public SeatController(TheatreRepository theatreRepository,
                          MovieSessionRepository movieSessionRepository,
                          MovieRepository movieRepository) {
        this.theatreRepository = theatreRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatApi() {
        return this.theatreRepository.findAll();
    }

    @GetMapping("/showing-seat/{movieId}")
    public String showSeat(@PathVariable Long movieId, Model model) {
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        List<MovieSession> movieSessions = movieSessionRepository
                .findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
        List<Theatre> theatres = theatreRepository.findAll();
        Movie movie = movieRepository.findMovieById(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSessions", movieSessions);
        model.addAttribute("theatres", theatres);
        return "show-seat";
    }

}
