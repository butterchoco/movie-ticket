package com.adpro.movie;

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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieSessionCreator {

    private MovieRepository movieRepository;
    private MovieSessionRepository movieSessionRepository;
    private TheatreRepository theatreRepository;
    public static int[] PEAK_SHOW_TIME = new int[]{10, 13, 17, 20};
    public static int[] NORMAL_SHOW_TIME = new int[]{13, 20};

    @Autowired
    public MovieSessionCreator(MovieRepository movieRepository,
                               MovieSessionRepository movieSessionRepository,
                               TheatreRepository theatreRepository) {
        this.movieRepository = movieRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.theatreRepository = theatreRepository;
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
}
