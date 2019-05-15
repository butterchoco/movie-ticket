package com.adpro.movie;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieRestController {

    private MovieService movieService;

    @Autowired
    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RequestMapping("/api/movies/showing")
    public List<Movie> movies() {
        return movieService.getTodayShowingMovies();
    }

    @RequestMapping("/api/movies/upcoming")
    public List<Movie> upcomingMovies() {
        return movieService.getTodayUpcomingMovies();
    }

    @RequestMapping("/api/movie/{movieId}")
    public List<MovieSession> movieSessions(@PathVariable Long movieId) {
        return movieService.getTodayMovieSessions(movieId);
    }

    @RequestMapping("/api/movie/session/{movieSessionId}")
    public MovieSession movieSession(@PathVariable Long movieSessionId) {
        return movieService.getMovieSession(movieSessionId);
    }
}
