package com.adpro.movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieRestController {

    private MovieSessionRepository movieSessionRepository;
    private MovieListProxy movieListProxy;

    @Autowired
    public MovieRestController(MovieSessionRepository movieSessionRepository,
                           MovieListProxy movieListProxy) {
        this.movieSessionRepository = movieSessionRepository;
        this.movieListProxy = movieListProxy;
    }

    @PostMapping("/movies")
    public List<Movie> movies(Model model) {
        return movieListProxy.findMoviesByReleaseDateAfter(LocalDate.now().minusDays(7));
    }

    @PostMapping("/movie/{movieId}")
    public List<MovieSession> movieSessions(@PathVariable Long movieId) {
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        return movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
    }

    @PostMapping("/movie/session/{movieSessionId}")
    public MovieSession movieSession(@PathVariable Long movieSessionId) {
        return movieSessionRepository.findById(movieSessionId).orElse(null);
    }
}
