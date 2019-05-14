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
        return new RedirectView("/movies/showing");
    }

    @RequestMapping("/movies/showing")
    public String movies(Model model) {
        List<Movie> movies = movieListProxy.findMoviesByReleaseDateAfterAndReleaseDateBefore(
                LocalDate.now().minusDays(MovieListProxy.DAYS_SHOWED), LocalDate.now());
        model.addAttribute("title", "Now Showing");
        model.addAttribute("movies", movies);
        return "movies";
    }

    @RequestMapping("/movies/upcoming")
    public String upcomingMovies(Model model) {
        List<Movie> movies = movieListProxy.findMoviesByReleaseDateAfter(LocalDate.now());
        model.addAttribute("title", "Upcoming Movies");
        model.addAttribute("movies", movies);
        return "movies";
    }
}
