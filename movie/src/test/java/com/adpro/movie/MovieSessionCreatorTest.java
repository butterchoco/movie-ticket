package com.adpro.movie;

import static org.mockito.BDDMockito.*;

import com.adpro.TestConfig;
import com.adpro.seat.Theatre;
import com.adpro.seat.TheatreRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class MovieSessionCreatorTest {

    @MockBean
    private MovieSessionRepository movieSessionRepository;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private TheatreRepository theatreRepository;

    private MovieSessionCreator movieSessionCreator;

    @Before
    public void init() {
        movieSessionCreator = Mockito.spy(
                new MovieSessionCreator(movieRepository, movieSessionRepository, theatreRepository));
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

        given(movieRepository.findMoviesByReleaseDateAfterAndReleaseDateBefore(any(), any()))
                .willReturn(List.of(newMovie, oldMovie));

        given(movieSessionRepository.findMovieSessionsByStartTimeAfter(any()))
                .willReturn(Collections.emptyList());


        given(theatreRepository.findAllByOrderByDescriptionDesc())
                .willReturn(theatreList);

        movieSessionCreator.checkExistOrCreateMovieSession();
        then(movieSessionCreator)
                .should()
                .createMovieSession(any());
    }
}