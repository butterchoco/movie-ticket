package com.adpro.movie;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.adpro.TestConfig;
import com.adpro.movie.tmdb.FullTMDBMovie;
import com.adpro.movie.tmdb.PartialTMDBMovie;
import com.adpro.movie.tmdb.TMDBRepository;
import com.adpro.seat.Theatre;
import com.adpro.seat.TheatreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class SchedulerTest {

    @MockBean
    private TMDBRepository tmdbRepository;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private MovieSessionRepository movieSessionRepository;

    @MockBean
    private TheatreRepository theatreRepository;

    private Scheduler scheduler;

    @Before
    public void init() {
         scheduler = Mockito.spy(new Scheduler(
                tmdbRepository,
                movieRepository,
                theatreRepository,
                movieSessionRepository));
    }

    @Test
    public void givenNewMovie_thenInsertToDB() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        PartialTMDBMovie oldTMDBMovie = mapper.readValue("{\"id\": 1, " +
                "\"original_title\": \"Fairuzi Adventures\"}", PartialTMDBMovie.class);

        PartialTMDBMovie newTMDBMovie = mapper.readValue("{\"id\": 2," +
                "\"original_title\": \"[BUKAN] Fairuzi Adventures\"}", PartialTMDBMovie.class);

        FullTMDBMovie fullNewTMDBMovie = mapper.readValue("{\"id\": 2," +
                "\"original_title\": \"[BUKAN] Fairuzi Adventures\"," +
                "\"runtime\": 120}", FullTMDBMovie.class);

        Movie oldMovie = Movie.builder()
                .tmdbId(1L)
                .name("Fairuzi Adventures")
                .build();

        given(tmdbRepository.getLastMovies())
                .willReturn(List.of(oldTMDBMovie, newTMDBMovie));

        given(movieRepository.findByTmdbIdIn(any()))
                .willReturn(List.of(oldMovie));

        given(tmdbRepository.getMovie(2L))
                .willReturn(fullNewTMDBMovie);

        scheduler.updateMovieList();

        then(tmdbRepository)
                .should()
                .getMovie(2L);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void givenNoMovieSessionAlreadyCreatedForToday_thenCreateNewMovieSessions() {
        Movie newMovie = Movie.builder()
                .name("Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now())
                .build();

        Movie oldMovie = Movie.builder()
                .name("[BUKAN] Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now().minusDays(10))
                .build();

        Theatre theatre = new Theatre("A", 50);
        List<Theatre> theatreList = mock(ArrayList.class);
        given(theatreList.get(anyInt()))
                .willReturn(theatre);

        given(theatreList.size())
                .willReturn(1);

        given(movieRepository.findMoviesByReleaseDateAfterAndReleaseDateBefore(any(), any()))
                .willReturn(List.of(newMovie, oldMovie));

        given(movieSessionRepository.findMovieSessionsByStartTimeAfter(any()))
                .willReturn(Collections.emptyList());

        given(theatreRepository.findAllByOrderByDescriptionDesc())
                .willReturn(theatreList);

        scheduler.checkExistOrCreateMovieSession();
        verify(scheduler)
                .createMovieSession(any());
    }

    @Test
    public void givenUpdateMovieSessionList_thenDoAllOperations() {
        scheduler.updateMovieSessionList();
        verify(scheduler)
                .updateMovieList();
        verify(scheduler)
                .checkExistOrCreateTheatre();
        verify(scheduler)
                .checkExistOrCreateMovieSession();
    }

    @Test
    public void givenNoTheatre_thenCreateTheatres() {
        given(theatreRepository.count())
                .willReturn(0L);
        scheduler.checkExistOrCreateTheatre();
        verify(theatreRepository)
                .saveAll(any());
    }

    @Test
    public void givenInsufficientTheatre_thenThrowRuntimeError() {
        Movie movie = Movie.builder()
                .name("Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now())
                .build();

        given(theatreRepository.findAllByOrderByDescriptionDesc())
                .willReturn(Collections.emptyList());
        try {
            scheduler.createMovieSession(List.of(movie));
            fail("Should throw Runtime Exception");
        } catch (RuntimeException e) {}
    }

    @Test
    @SneakyThrows
    public void givenMissingMovies_thenUpdateDBMovie() {
        ObjectMapper mapper = new ObjectMapper();
        FullTMDBMovie fullTMDBMovie = mapper.readValue("{\"id\": 1," +
                "\"original_title\": \"Fairuzi Adventures\"," +
                "\"runtime\": 120}", FullTMDBMovie.class);
        given(tmdbRepository.getMovie(any()))
                .willReturn(fullTMDBMovie);

        scheduler.getAndAddToDB(List.of(1L));
        then(movieRepository)
                .should()
                .saveAll(any());
    }
}
