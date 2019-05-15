package com.adpro.movie;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.adpro.TestConfig;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class MovieListProxyTest {
    @MockBean
    private MovieRepository movieRepository;

    private MovieListProxy movieListProxy;

    @Before
    public void init() {
        this.movieListProxy = new MovieListProxy(movieRepository);
    }

    @Test
    public void givenUpcomingUpdatedRecently_thenReturnFromMemory() {
        movieListProxy.findMoviesByReleaseDateAfter(LocalDate.now());

        movieListProxy.findMoviesByReleaseDateAfter(LocalDate.now());
        verify(movieRepository, times(1))
                .findMoviesByReleaseDateAfter(LocalDate.now());
    }

    @Test
    public void givenShowingUpdatedRecently_thenReturnFromMemory() {
        movieListProxy.findMoviesByReleaseDateBetween(
                LocalDate.now(), LocalDate.now());

        movieListProxy.findMoviesByReleaseDateBetween(
                LocalDate.now(), LocalDate.now());
        verify(movieRepository, times(1))
                .findMoviesByReleaseDateBetween(
                        LocalDate.now().minusDays(MovieListProxy.DAYS_SHOWED), LocalDate.now());
    }
}
