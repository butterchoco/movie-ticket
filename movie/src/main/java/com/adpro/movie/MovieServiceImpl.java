package com.adpro.movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieListRepository movieListRepository;
    private MovieSessionRepository movieSessionRepository;
    private MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieListRepository movieListRepository,
                            MovieSessionRepository movieSessionRepository,
                            MovieRepository movieRepository) {
        this.movieListRepository = movieListRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getTodayShowingMovies() {
        return movieListRepository.findMoviesByReleaseDateBetween(
                LocalDate.now().minusDays(MovieListProxy.DAYS_SHOWED), LocalDate.now());
    }

    @Override
    public List<Movie> getTodayUpcomingMovies() {
        return movieListRepository.findMoviesByReleaseDateAfter(LocalDate.now());
    }

    @Override
    public List<MovieSession> getTodayMovieSessions(Long movieId) {
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        return movieSessionRepository
                .findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
    }

    @Override
    public Movie getMovie(Long movieId) {
        return movieRepository.findMovieById(movieId);
    }

    @Override
    public MovieSession getMovieSession(Long movieSessionId) {
        return movieSessionRepository.findById(movieSessionId).orElse(null);
    }
}
