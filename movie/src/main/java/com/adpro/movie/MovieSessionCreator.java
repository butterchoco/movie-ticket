package com.adpro.movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieSessionCreator {

    private MovieRepository movieRepository;
    private MovieSessionRepository movieSessionRepository;

    /**
     * For test.
     */
    protected MovieSessionCreator() {}

    @Autowired
    public MovieSessionCreator(MovieRepository movieRepository, MovieSessionRepository movieSessionRepository) {
        this.movieRepository = movieRepository;
        this.movieSessionRepository = movieSessionRepository;
    }

    public void checkExistOrCreateMovieSession() {
        List<Movie> movies = movieRepository.findMoviesByReleaseDateAfterAndReleaseDateBefore(
                LocalDate.now().minusDays(14), LocalDate.now());

        List<MovieSession> alreadyCreatedTodayMovieSession = movieSessionRepository.findMovieSessionsByStartTimeAfter(
                LocalDate.now().atStartOfDay());

        if (alreadyCreatedTodayMovieSession.size() == 0) {
            createMovieSession(movies);
        }
    }

    public void createMovieSession(List<Movie> movies) {
        LocalDate dateNow = LocalDate.now();
        List<MovieSession> willBeInsertedMovieSession = new ArrayList<>();
        for (Movie movie : movies) {
            long alreadyShowedFor = Period.between(movie.getReleaseDate(), dateNow).getDays();
            if (alreadyShowedFor < 5) {
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(10, 0))));
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(13, 0))));
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(17, 0))));
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(20, 0))));
            } else {
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(13, 0))));
                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(20, 0))));
            }
        }
        movieSessionRepository.saveAll(willBeInsertedMovieSession);
    }
}
