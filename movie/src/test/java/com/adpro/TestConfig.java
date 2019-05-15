package com.adpro;

import static org.mockito.Mockito.mock;

import com.adpro.movie.MovieListRepository;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.Scheduler;
import com.adpro.movie.tmdb.TmdbClient;
import com.adpro.movie.tmdb.TmdbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;

@EnableAutoConfiguration
@Configuration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MainConfig.class})})
public class TestConfig {

    /**
     * Don't hit TMDB API on test.
     * @return Mocked TMDBRepository.
     */
    @Bean
    @Primary
    public TmdbRepository getTmdbRepository() {
        return mock(TmdbRepository.class);
    }

    @Bean
    public TmdbClient getTmdbClient() {
        return mock(TmdbClient.class);
    }

    @Bean
    @Primary
    public Scheduler getScheduler() {
        return mock(Scheduler.class);
    }

    /**
     * Don't proxy movie list.
     * @param movieRepository movie repository bean.
     * @return movie repository.
     */
    @Bean
    @Primary
    @Autowired
    public MovieListRepository getMovieListRepository(MovieRepository movieRepository) {
        return movieRepository;
    }

}
