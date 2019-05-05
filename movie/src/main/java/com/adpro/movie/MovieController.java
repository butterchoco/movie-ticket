package com.adpro.movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MovieController {

    private MovieRepository movieRepository;
    private MovieSessionRepository movieSessionRepository;
    private MovieListProxy movieListProxy;

    @Autowired
    public MovieController(MovieRepository movieRepository,
                           MovieSessionRepository movieSessionRepository,
                           MovieListProxy movieListProxy) {
        this.movieRepository = movieRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.movieListProxy = movieListProxy;
    }

    @RequestMapping("/")
    public RedirectView redirectToMovies() {
        return new RedirectView("/movies");
    }

    @RequestMapping("/movies")
    public String movies(Model model) {
        List<Movie> movies = movieListProxy.findMoviesByReleaseDateAfter(LocalDate.now().minusDays(7));
        model.addAttribute("movies", movies);
        return "movies";
    }

    @RequestMapping("/movie/{movieId}")
    public List<MovieSession> movieSessions(@PathVariable Long movieId) {
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        return movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
    }
}
