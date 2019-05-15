package com.adpro.movie;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cache the result from DB into memory.
 */
@Component
public class MovieListProxy implements MovieListRepository {
    private MovieRepository movieRepository;
    private List<Movie> lastShowingMovies;
    private List<Movie> lastUpcomingMovies;
    private LocalDateTime lastUpdate;

    public static final int DAYS_SHOWED = 7;

    @Autowired
    public MovieListProxy(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        lastShowingMovies = new ArrayList<>();
        lastUpcomingMovies = new ArrayList<>();
        lastUpdate = LocalDateTime.MIN;
    }

    private void updateAllMovieList() {
        lastUpcomingMovies = movieRepository.findMoviesByReleaseDateAfter(LocalDate.now());
        lastShowingMovies = movieRepository.findMoviesByReleaseDateBetween(
                LocalDate.now().minusDays(DAYS_SHOWED), LocalDate.now());
        lastUpdate = LocalDateTime.now();
    }

    public List<Movie> findMoviesByReleaseDateAfter(LocalDate date) {
        if (Duration.between(lastUpdate, LocalDateTime.now()).toDays() >= 1) {
            updateAllMovieList();
        }
        return lastUpcomingMovies;
    }

    public List<Movie> findMoviesByReleaseDateBetween(
            LocalDate after, LocalDate before) {
        if (Duration.between(lastUpdate, LocalDateTime.now()).toDays() >= 1) {
            updateAllMovieList();
        }
        return lastShowingMovies;
    }
}
