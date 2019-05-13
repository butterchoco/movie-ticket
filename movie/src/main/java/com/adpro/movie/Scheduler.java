package com.adpro.movie;

import com.adpro.movie.tmdb.FullTMDBMovie;
import com.adpro.movie.tmdb.PartialTMDBMovie;
import com.adpro.movie.tmdb.TMDBMovie;
import com.adpro.movie.tmdb.TMDBRepository;
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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private TMDBRepository tmdbRepository;
    private MovieRepository movieRepository;
    private TheatreRepository theatreRepository;
    private MovieSessionRepository movieSessionRepository;
    public static int[] PEAK_SHOW_TIME = new int[]{10, 13, 17, 20};
    public static int[] NORMAL_SHOW_TIME = new int[]{13, 20};

    @Autowired
    public Scheduler(TMDBRepository tmdbRepository,
                     MovieRepository movieRepository,
                     TheatreRepository theatreRepository,
                     MovieSessionRepository movieSessionRepository) {
        this.tmdbRepository = tmdbRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.movieSessionRepository = movieSessionRepository;
        System.out.println(tmdbRepository + " isi");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void midnightCron() {
        updateMovieSessionList();
    }

    @PostConstruct
    public void postConstruct() {
        updateMovieSessionList();
    }

    public void updateMovieSessionList() {
        updateMovieList();
        checkExistOrCreateMovieSession();
        checkExistOrCreateTheatre();
    }

    void updateMovieList() {
        List<PartialTMDBMovie> movies = tmdbRepository.getLastMovies();
        List<Long> movieIds = new ArrayList<>();
        for (TMDBMovie movie: movies) {
            movieIds.add(movie.getId());
        }

        Set<Long> existingMovieTmdbIds = movieRepository.findByTmdbIdIn(movieIds)
                .stream()
                .map(Movie::getTmdbId)
                .collect(Collectors.toSet());

        List<Movie> notExistMovies = new ArrayList<>();
        for (PartialTMDBMovie movie: movies) {
            if (!existingMovieTmdbIds.contains(movie.getId())) {
                FullTMDBMovie tmdbMovie = tmdbRepository.getMovie(movie.getId());
                notExistMovies.add(Movie.fromTMDBMovie(tmdbMovie));
            }
        }
        movieRepository.saveAll(notExistMovies);
    }

    void checkExistOrCreateMovieSession() {
        System.out.println(tmdbRepository);
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
        Map<Integer, List<Theatre>> availableTheatreOfShowTime = new HashMap<>();
        for (int showTime : PEAK_SHOW_TIME) {
            availableTheatreOfShowTime.put(showTime, theatreRepository.findAllByOrderByDescriptionDesc());
        }

        for (Movie movie : movies) {
            long alreadyShowedFor = Period.between(movie.getReleaseDate(), dateNow).getDays();
            int[] showTimes = NORMAL_SHOW_TIME;
            if (alreadyShowedFor < 3) {
                showTimes = PEAK_SHOW_TIME;
            }

            for (int showTime : showTimes) {
                List<Theatre> availableTheatre = availableTheatreOfShowTime.get(showTime);
                Theatre usedTheatre = availableTheatre.get(availableTheatre.size() - 1);
                availableTheatre.remove(availableTheatre.size() - 1);

                willBeInsertedMovieSession.add(new MovieSession(movie,
                        LocalDateTime.of(dateNow, LocalTime.of(showTime, 0)),
                        usedTheatre));
            }
        }
        movieSessionRepository.saveAll(willBeInsertedMovieSession);
    }

    void checkExistOrCreateTheatre() {
        long count = theatreRepository.count();
        if (count == 0) {
            List<Theatre> theatreList = new ArrayList<>();
            theatreList.add(new Theatre("A", 50));
            theatreList.add(new Theatre("B", 40));
            theatreList.add(new Theatre("C", 60));
            theatreList.add(new Theatre("D", 70));
            theatreList.add(new Theatre("E", 70));
            theatreList.add(new Theatre("F", 70));
            theatreRepository.saveAll(theatreList);
        }
    }
}
