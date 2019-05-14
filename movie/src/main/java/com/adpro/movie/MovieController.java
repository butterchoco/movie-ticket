package com.adpro.movie;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping("/")
    public RedirectView redirectToMovies() {
        return new RedirectView("/movies/showing");
    }

    @RequestMapping("/movies/showing")
    public String todayShowingMovies(Model model) {
        List<Movie> showingMovies = movieService.getTodayShowingMovies();
        model.addAttribute("title", "Now Showing");
        model.addAttribute("movies", showingMovies);
        return "movies";
    }

    @RequestMapping("/movies/upcoming")
    public String todayUpcomingMovies(Model model) {
        List<Movie> upcomingMovies = movieService.getTodayUpcomingMovies();
        model.addAttribute("title", "Upcoming Movies");
        model.addAttribute("movies", upcomingMovies);
        return "movies";
    }

    @RequestMapping("/movie/{movieId}")
    public String movieAndTodaySessions(@PathVariable Long movieId, Model model) {
        List<MovieSession> todayMovieSessions = movieService.getTodayMovieSessions(movieId);
        Movie movie = movieService.getMovie(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSessions", todayMovieSessions);
        return "movie";
    }
}
