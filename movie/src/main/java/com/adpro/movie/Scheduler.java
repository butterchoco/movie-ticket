package com.adpro.movie;

import com.adpro.movie.tmdb.FullTmdbMovie;
import com.adpro.movie.tmdb.PartialTmdbMovie;
import com.adpro.movie.tmdb.TmdbMovie;
import com.adpro.movie.tmdb.TmdbRepository;
import com.adpro.seat.Theatre;
import com.adpro.seat.TheatreRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private TmdbRepository tmdbRepository;
    private MovieRepository movieRepository;
    private TheatreRepository theatreRepository;
    private MovieSessionRepository movieSessionRepository;
    public static int[] PEAK_SHOW_TIME = new int[]{10, 13, 17, 20};
    public static int[] NORMAL_SHOW_TIME = new int[]{13, 20};

    @Autowired
    public Scheduler(TmdbRepository tmdbRepository,
                     MovieRepository movieRepository,
                     TheatreRepository theatreRepository,
                     MovieSessionRepository movieSessionRepository) {
        this.tmdbRepository = tmdbRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.movieSessionRepository = movieSessionRepository;
    }

    public void updateMovieSessionList() {
        CompletableFuture.allOf(
                updateMovieList(),
                checkExistOrCreateTheatre()).join();
        checkExistOrCreateMovieSession();
    }

    public CompletableFuture<Void> updateMovieList() {
        List<PartialTmdbMovie> movies = tmdbRepository.getLastMovies();
        List<Long> movieIds = new ArrayList<>();
        for (TmdbMovie movie: movies) {
            movieIds.add(movie.getId());
        }

        Set<Long> existingMovieTmdbIds = movieRepository.findByTmdbIdIn(movieIds)
                .stream()
                .map(Movie::getTmdbId)
                .collect(Collectors.toSet());

        List<Long> notExistMovieIds = new ArrayList<>();
        for (PartialTmdbMovie movie: movies) {
            if (!existingMovieTmdbIds.contains(movie.getId())) {
                notExistMovieIds.add(movie.getId());
            }
        }
        getAndAddToDB(notExistMovieIds);
        return CompletableFuture.completedFuture(null);
    }

    void getAndAddToDB(List<Long> movieIds) {
        List<CompletableFuture<FullTmdbMovie>> retrievedMovies = new ArrayList<>();
        for (Long id: movieIds) {
            retrievedMovies.add(CompletableFuture.supplyAsync(() -> tmdbRepository.getMovie(id)));
        }
        CompletableFuture
                .allOf(retrievedMovies.toArray(new CompletableFuture[0]))
                .join();

        List<Movie> movies = retrievedMovies
                .stream()
                .map(CompletableFuture::join)
                .map(Movie::fromTmdbMovie)
                .collect(Collectors.toList());
        movieRepository.saveAll(movies);
    }

    void checkExistOrCreateMovieSession() {
        List<Movie> movies = movieRepository.findMoviesByReleaseDateBetween(
                LocalDate.now().minusDays(14), LocalDate.now());

        List<MovieSession> alreadyCreatedTodayMovieSession = movieSessionRepository
                .findMovieSessionsByStartTimeAfter(LocalDate.now().atStartOfDay());

        if (alreadyCreatedTodayMovieSession.size() == 0) {
            createMovieSession(movies);
        }
    }

    void createMovieSession(List<Movie> movies) {
        LocalDate dateNow = LocalDate.now();
        List<MovieSession> willBeInsertedMovieSession = new ArrayList<>();
        Map<Integer, List<Theatre>> availableTheatreOfShowTime = new HashMap<>();
        for (int showTime : PEAK_SHOW_TIME) {
            availableTheatreOfShowTime.put(showTime,
                    theatreRepository.findAllByOrderByDescriptionDesc());
        }

        for (Movie movie : movies) {
            long alreadyShowedFor = Period.between(movie.getReleaseDate(), dateNow).getDays();
            int[] showTimes = NORMAL_SHOW_TIME;
            if (alreadyShowedFor < 3) {
                showTimes = PEAK_SHOW_TIME;
            }

            for (int showTime : showTimes) {
                List<Theatre> availableTheatre = availableTheatreOfShowTime.get(showTime);
                if (availableTheatre.size() == 0) {
                    throw new RuntimeException("Not enough theatre to show movies");
                }
                Theatre usedTheatre = availableTheatre.get(availableTheatre.size() - 1);
                availableTheatre.remove(availableTheatre.size() - 1);

                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(showTime, 0)),
                        usedTheatre));
            }
        }
        movieSessionRepository.saveAll(willBeInsertedMovieSession);
    }

    public CompletableFuture<Void> checkExistOrCreateTheatre() {
        long count = theatreRepository.count();
        if (count == 0) {
            List<Theatre> theatreList = new ArrayList<>();
            theatreList.add(new Theatre("A", 40));
            theatreList.add(new Theatre("B", 40));
            theatreList.add(new Theatre("C", 50));
            theatreList.add(new Theatre("D", 50));
            theatreList.add(new Theatre("E", 50));
            theatreList.add(new Theatre("F", 50));
            theatreList.add(new Theatre("G", 60));
            theatreList.add(new Theatre("H", 60));
            theatreList.add(new Theatre("I", 60));
            theatreList.add(new Theatre("J", 60));
            theatreList.add(new Theatre("K", 60));
            theatreList.add(new Theatre("L", 70));
            theatreList.add(new Theatre("M", 70));
            theatreList.add(new Theatre("N", 70));
            theatreList.add(new Theatre("O", 70));
            theatreRepository.saveAll(theatreList);
        }
        return CompletableFuture.completedFuture(null);
    }
}
